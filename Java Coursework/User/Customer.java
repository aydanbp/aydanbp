package User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import room.Room;

public class Customer extends User {
    static Scanner s = new Scanner(System.in);
    private String name, DoB;
    private int pNum;
    private Boolean paid;
    private Boolean chkin;
    private static int day, Nights;
    private static boolean fmt = false;
    private static boolean lclEsc = false;
    private String bkID;
    private static String Inp;

    static HashMap<Integer, Room> sRooms = new HashMap<>(); //Standard Room
    static HashMap<Integer, Room> dRooms = new HashMap<>();  // Deluxe Room
    static HashMap<Integer, Room> suRooms = new HashMap<>();  // Suite Room
    

    public Customer(String nm, String sNm, String DoB) {
        name = nm + sNm;
        this.DoB = DoB;

    }

    public void display() { // Mainly for testing
        System.out.println("Customer's Name" + name + "\n"
                + "Customer's Date of birth" + DoB);
    }

    public static void list(List rooms, String[][] bookID) {
        do {
            System.out.print("Input day (Format YYYY-MM-DD): \t");
            try {
                day = LocalDate.parse(s.next()).getDayOfYear();
                fmt = true;
            } catch (Exception e) {
                System.out.println("Exception occured:\t" + e + "\tTry again!");
            }
        } while (!fmt);

        for (int i = 0; i < rooms.size(); i++) {
            if (bookID[day][Integer.parseInt(((Room) rooms.get(i)).getRoomNo())] == null) {
                ((Room) rooms.get(i)).display();
            }
        }

    }

    public static void listOucType(List rooms, String[][] bookID) {
        List Des = new ArrayList<>(); //specific type lists are passed here to they can be checked against the day
        String type;
        int cont = rooms.size(), ocu;
        for (int i = 1; i < cont; i++) {
            switch (((Room) rooms.get(i - 1)).type()) { // add rooms to list specific to type
                case "standard":
                    sRooms.put(((Room) rooms.get(i-1)).getOcup(),(Room) rooms.get(i - 1));
                    break;
                case "deluxe":
                dRooms.put(((Room) rooms.get(i-1)).getOcup(),(Room) rooms.get(i - 1));
                    break;
                case "suite":
                suRooms.put(((Room) rooms.get(i-1)).getOcup(),(Room) rooms.get(i - 1));
                    break;
            }
        }
        System.out.print("Input day (Format YYYY-MM-DD): \t");
        do {
            try {
                day = LocalDate.parse(s.next()).getDayOfYear();
                fmt = true;
            } catch (Exception e) {
                System.out.println("Exception occured:\t" + e + "\tTry again!");
            }
        } while (!fmt);
        System.out.print("Room type");
        type = s.next();
        System.out.print("Occupancy");
        
        fmt=false;
        do {
            try {
                ocu = s.nextInt();
                fmt = true;
            } catch (NullPointerException e) {
                System.out.println("Doesn't exist... Try again");
            }
        } while (!fmt);
        fmt = false;
        
        switch (type.toLowerCase()) {// pass back to list so can be checked against booking 1-1
            case "standard":
                Des.add(sRooms);
                break;
                case "deluxe":
                Des.add(dRooms);
                break;
                case "suite":
                Des.add(suRooms);
                break;
        }
        for (int i = 0; i < Des.size(); i++) {
            if (bookID[day][Integer.parseInt(((Room) Des.get(i)).getRoomNo())] == null) {
                ((Room) Des.get(i)).display();
            }
        }
    }

    public static void booking(Map RoomNo, Customer C1, String[][] bookID) {// room bookings
        int [] dates = new int[366];
        String x, uCode;
        boolean xLeap = false;
        int i;
        do {
            System.out.print("Input first day (Format YYYY-MM-DD): \t");
            do {
                try {
                    x = s.next();
                    day = LocalDate.parse(x).getDayOfYear();
                    xLeap = LocalDate.parse(x).isLeapYear();
                    fmt = true;
                } catch (Exception e) {
                    System.out.println("Exception occured:\t" + e + "\tTry again!");
                }
            } while (!fmt);
            System.out.print("Input last day (Format YYYY-MM-DD): \t");
            fmt = false;
            do {
                try {
                    Nights = LocalDate.parse(s.next()).getDayOfYear();
                    fmt = true;
                } catch (Exception e) {
                    System.out.println("Exception occured:\t" + e + "\tTry again!");
                }
            } while (!fmt);

            System.out.println("What room would you like?");
            Inp = s.next();
            ((Room) RoomNo.get(Inp)).display();
            int dif = Math.abs(Nights-day);
            double Total = ((Room) RoomNo.get(Inp)).price()*dif;
            System.out.println("Total price\n£"+Total+"\nIs this ok?\t>>>");
            lclEsc = AgreeDisagree(s.next());
        } while (!lclEsc);
        uCode = "code"; //C1.toString() + RoomNo.get(Inp).toString();
        for (i = 0; day != Nights; i++) {
            if (day == 366 && xLeap == true){
                bookID[day][Integer.parseInt(Inp)] = uCode;
                day=1;
            }
            else if (day == 365 && xLeap == false){
                bookID[day][Integer.parseInt(Inp)] = uCode;
                day=1;
            }
            else{
                bookID[day][Integer.parseInt(Inp)] = uCode;
                day++;
            }
        }
        System.out.println("Your Booking ID\t" +
                uCode);
    }

    public static int delete(String[][] bookID) { // Unfinished
        boolean confirm;
        String Inp;
        int i,j, x = 0;
        System.out.print("""
                         WARNING! YOU'RE ABOUT TO DELETE YOUR BOOKING. THIS CANNOT BE UNDONE!!!
                         Is this ok?\t>>>""");
        confirm = AgreeDisagree(s.next());
        if (confirm = true) {
        System.out.println("Input Unique booking ID\t>>>");
        Inp = s.next();
        System.out.println("Room Number\t>>>");
        j = s.nextInt();
        try {
            for (i = 1; i < bookID.length; i++) {
                if (bookID[i][j].equals(Inp)){
                    bookID[i][j] = null;
                    x++;
                }
            }
        } catch (Exception e) {
            System.out.println("An error occured\n"+e);
        }
    }
        return x;
    }

}
