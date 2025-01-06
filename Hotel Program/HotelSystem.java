
import CustomExceptions.CustomFormatException;
import CustomExceptions.DoubleBook;
import CustomExceptions.InvalidDateTypeException;
import CustomExceptions.InvalidRmTypException;
import CustomExceptions.Overlappable;
import Sorters.PriceCompare;
import Sorters.RmNumCmp;
import Sorters.RmNumRev;
import User.Booking;
import User.Customer;
import User.HotelCustomer;
import User.HotelManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import room.DeluxeRoom;
import static room.RmGen.RmGen;
import room.Room;
import room.Standard;
import room.SuiteRoom;

/*
The purpose of this program is to create a text based interface where a customer can book different types of rooms 
and an admin can create and destroy rooms
VERSION 1.0b
 */
public class HotelSystem implements HotelCustomer, HotelManager, Overlappable {

    static HotelSystem h = new HotelSystem();
    static List<Room> rooms = RmGen();
    static Scanner s = new Scanner(System.in);
    static LocalDate Today = LocalDate.now();

    public static void main(String[] args) { // main menu

        System.out.println("///CUSTOMER HOTEL MENU///");
        System.out.println("""
                        Command List:
                List Rooms by:
                    date (ASC) -> time
                    day, occupancy and type -> type
                Room booking -> book
                Delete Booking -> delete
                Admin Menu -> admin
                    """);

        Customer C1 = null;
        Booking B1 = null;
        boolean xLeap = Today.isLeapYear();
        List<Booking> lBookings = new ArrayList<Booking>();
        boolean Exit = false;
        
        do {
            HashMap<String, Room> RoomNo = new HashMap<>();
            HashMap<String, Booking> Bookings = new HashMap<>();
            HashMap<Integer, Room> desiredRooms = new HashMap<>(); //Room map that uses Room number for keys
            Collections.sort(rooms, new PriceCompare());
            int cont = rooms.size(), cont2 = lBookings.size();
            for (int i = 0; i < cont; i++) {
                RoomNo.put(rooms.get(i).getRoomNo(), rooms.get(i)); // creates Room Number list
            }
            for (int i = 0; i < cont2; i++) {
                Bookings.put(lBookings.get(i).getRoomNo(), lBookings.get(i)); //Creates booking list
            }
            System.out.print("Next Command\t>>> ");
            String Query = s.next();
            switch (Query.toLowerCase()) {

                case "admin":
                    admin(rooms, lBookings);
                    break;
                case "time":
                    h.list(Bookings, lBookings, RoomNo, xLeap);
                    break;
                case "type":
                    h.listOucType(desiredRooms, Bookings, lBookings, RoomNo, xLeap);
                    break;
                case "book":
                    LocalDate DoB = null;
                    String pNum = "";
                    String eMl = "";
                    System.out.print("Please input first & last names:\t>>> ");
                    String Name = s.next();
                    String sName = s.next();
                    Name = Name + " " + sName;
                    do {
                        System.out.print("Date of birth (Format YYYY-MM-DD)\t>>> ");
                        try {
                            DoB = LocalDate.parse(s.next());
                            if (DoB.toEpochDay() > LocalDate.now().toEpochDay() - 6570) { //If DOB isn't atleast 18 years from today, error is thrown
                                throw new InvalidDateTypeException("Younger than 18 years old");
                            } else if (DoB.toEpochDay() < LocalDate.now().toEpochDay() - 43800) { 
                                throw new InvalidDateTypeException("Older than 120 years old");
                            }
                            Exit = true;
                        } catch (DateTimeParseException e) {
                            System.out.println("Exception occured:\t Invalid date format occured! \tTry again!");
                        } catch (InvalidDateTypeException ex) {
                            System.out.println("Valid format but invalid Date of birth\t" + ex.getMessage() + "\tTry again!");
                        }
                    } while (!Exit);
                    Exit = false;

                    do {
                        System.out.print("Phone Number:\t>>> ");
                        try {

                            int Phone = s.nextInt();
                            pNum = "(+44) " + Phone;
                            if (pNum.length()<15 || pNum.length()>=17) {
                                throw new CustomFormatException("Number must be 10-11 digits");

                            }
                        } catch (CustomFormatException e) {
                            System.out.println(e.getMessage());
                        } catch (InputMismatchException ex) {
                            System.out.print("Number was not entered\n");
                            s.next();
                        }
                    } while (pNum.length()<15 || pNum.length()>=17);

                    do {
                        System.out.print("Email:\t>>> ");
                        try {
                            eMl = s.next();
                            if (!eMl.contains("@") || !eMl.contains(".")) {
                                throw new CustomFormatException("Email must contain '@' & '.'");
                            }
                        } catch (CustomFormatException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } while (!eMl.contains("@") && !eMl.contains("."));

                    System.out.println("Thank you!");
                    C1 = new Customer(Name, DoB, pNum, eMl);
                    h.booking(B1, C1, RoomNo, Bookings, lBookings);
                    break;
                case "delete":
                    boolean x = h.delete(lBookings);
                    if (!x) {
                        System.out.println("Booking deletion unsucessful");

                    } else {
                        System.out.println("Booking deletion successful!");
                    }
                    break;
                case "exit":
                    Exit = true;
                    break;
                default:
                    System.out.println("Command not found. Try again.");
            }
        } while (!Exit);
        System.out.println("Thank you & Goodbye!");
    }

    /*
 * Customer Related methods
     */
    @Override
    public boolean Overlap(Booking B1, Map Bookings, boolean xLeap, List lBookings) {
        if (lBookings.isEmpty()) {
            return false;
        }
        List<Integer> dates1 = new ArrayList<>();
        List<Integer> dates2 = new ArrayList<>();
        String RmNum = B1.getRoomNo();

        try {
            dates2.addAll(((Booking) Bookings.get(RmNum)).getDuration(xLeap));
        } catch (NullPointerException e) {
            return false;
        }
        
        dates1.addAll(B1.getDuration(xLeap));

        /*
         * Senario: Customer A has booked Monday to Wednesday
         * Customer C has booked Friday to Sunday
         * Customer B can only book Wednesday to Friday but is unaware of A&C
         * Remember this code is checking is there are two bookings
         */
        

        for (int i = 0; i < dates1.size() ; i++) { // if a date matches another date then overlap will be true

            for (int j = 0; j < dates2.size() ; j++) {
                if (Objects.equals(dates1.get(i), dates2.get(j))) {
                    return true;

                }

            }

        }

        return false;

    }

    @Override
    public void list(Map Bookings, List lBookings, Map RoomNo, boolean xLeap) {
        Integer day = 0;
        List< Room> Av = new ArrayList<>();
        Av.addAll(rooms);
        List< Room> noAv = new ArrayList<>();
        boolean fmt = false;
        
        day = 20;
        do {
            System.out.print("Input day (Format YYYY-MM-DD): \t");
            try {
                day = LocalDate.parse(s.next()).getDayOfYear();
                fmt = true;
            } catch (DateTimeParseException e) {
                System.out.println("Exception occured:\tInvalid date given\tTry again!");
            }
        } while (!fmt);
        
        for (int i = 0; i < lBookings.size(); i++) { // list of unavalible rooms
            List <Integer> date1 = (((Booking) lBookings.get(i)).getDuration(xLeap));
                for (int j = 0; j < date1.size(); j++) {
                    if (date1.get(j)==day) {
                        noAv.add(((Booking) lBookings.get(i)).getRoom());
                    }
                        
                    
                }

        }
        Av.removeAll(noAv);
        

        
        Collections.sort(Av, new PriceCompare());
        for (int i = 0; i < Av.size(); i++) {
            ((Room) Av.get(i)).display();
            System.out.println("------------------");
        }
        System.out.println(Av.size() + " rooms out of " + rooms.size()+ " avalible");
    }

    @Override
    public void listOucType(Map desiredRooms, Map Bookings, List lBookings, Map RoomNo, boolean xLeap) {
        List<Room> desRooms = new ArrayList<>();
        List< Room> Av = new ArrayList<>();
        Av.addAll(rooms);
        int day = 0;
        boolean fmt = false;
        Boolean Exit = false;
        String type = null;
        LocalDate x;
        List< Room> noAv = new ArrayList<>();
        Map<String, Room> DesRooms = RoomNo;
        int ocu = 0;
        Map<List, Room> Dates = new HashMap<>();


        do {
            System.out.print("Input day (Format YYYY-MM-DD): \t");
            try {
                x = LocalDate.parse(s.next());
                day = x.getDayOfYear();
                if (x.toEpochDay() < Today.toEpochDay()) {
                    throw new InvalidDateTypeException("Date entered cannot occur before today");

                }
                fmt = true;
            } catch (DateTimeParseException e) {
                System.out.println("Exception occured:\t Date not given.\tTry again!");
            } catch (InvalidDateTypeException ex) {
                System.out.println("Exception occured:\t" + ex.getMessage() + "\tTry again!");
            }
        } while (!fmt);
        fmt = false;


        for (int i = 0; i < lBookings.size(); i++) { // list of unavalible rooms
            List <Integer> date1 = (((Booking) lBookings.get(i)).getDuration(xLeap));
                for (int j = 0; j < date1.size(); j++) {
                    if (date1.get(j)==day) {
                        noAv.add(((Booking) lBookings.get(i)).getRoom());
                    }
                        
                    
                }

        }
        Av.removeAll(noAv);
        do {// same day loop
            do {
                System.out.print("Room type\t>>> ");
                try {
                    type = s.next();
                    if (!type.toLowerCase().contains("standard")
                            && !type.toLowerCase().contains("deluxe")
                            && !type.toLowerCase().contains("suite")) {
                        throw new InvalidRmTypException("Invalid room type selected");
                    }
                    fmt = true;
                } catch (InvalidRmTypException e) {
                    System.out.println(e.getMessage());
                }
            } while (!fmt);
            fmt = false;

            
            do {
                System.out.print("Occupancy\t>>> ");
                try {
                    ocu = s.nextInt();
                    fmt = true;
                } catch (InputMismatchException e) {
                    System.out.println("Number not detected... Try again!");
                    s.next();
                }
            } while (!fmt);
            fmt = false;
            for (int i = 0; i < Av.size(); i++) {
                if ((Av.get(i)).type().equals(type) && Av.get(i).getOcup() == ocu) {// pass back to list so can be checked against booking 1-1
                    desRooms.add(Av.get(i));
                }
            }

            for (int i = 0; i < desRooms.size(); i++) {
                desRooms.get(i).display();
                System.out.println("------------------");

            }
            System.out.println("Try again?");
            Exit = !AgreeDisagree.AgreeDisagree(s.next());
        } while (!Exit);
    }

    @Override
    public void booking(Booking B1, Customer C1, Map RoomNo, Map Bookings, List lBookings) {
        String Inp = null;
        boolean xLeap = false, fmt = false;
        Boolean lclEsc = false;
        int day, Nights;
        LocalDate x = null, y = null;
        int i;
        Double Total;
        do {

            do {
                System.out.print("Input arrival day (Format YYYY-MM-DD): \t>>> ");
                try {
                    x = LocalDate.parse(s.next());
                    day = x.getDayOfYear();
                    xLeap = x.isLeapYear();
                    if (x.toEpochDay() < LocalDate.now().toEpochDay()) {//if first date is earlier than today, error occurs
                        throw new InvalidDateTypeException("Date entered occured before today");
                    } else if (x.toEpochDay() > LocalDate.now().toEpochDay() + 183) { //if date entered is more than six months
                        throw new InvalidDateTypeException("Date must be within a six month period of today");
                    }
                    fmt = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Exception occured: \t'" + e.getParsedString() + "' is not a valid date");
                } catch (InvalidDateTypeException ex) {
                    System.out.println("We cannot accept this date:\n" + ex.getMessage());
                }
            } while (!fmt);
            fmt = false;
            do {
                System.out.print("Input departure day (Format YYYY-MM-DD): \t>>> ");
                try {
                    y = LocalDate.parse(s.next());
                    Nights = y.getDayOfYear();

                    if (x.toEpochDay() == y.toEpochDay()) { //if departure && arrival are the same
                        throw new InvalidDateTypeException("Checkout day cannot be after or same day as arrival");
                    } else if (x.toEpochDay() > y.toEpochDay()) { //if departure date is before arival
                        throw new InvalidDateTypeException("Departure date cannot be before arrival");
                    } else if (x.toEpochDay() + 182 < y.toEpochDay()) {//if departure is more than 6 months after arrival
                        throw new InvalidDateTypeException("Departure must occur before 6 months of arrival. Do you not have a home?");
                    }
                    fmt = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Exception occured: \t'" + e.getParsedString() + "' is not a valid date");
                } catch (InvalidDateTypeException ex) {
                    System.out.println("We cannot accept this date:\n" + ex.getMessage());
                }
            } while (!fmt);
            fmt = false;

            do {
                System.out.print("What room would you like? (Format: ###)\t>>> ");
                try {
                    Inp = s.next();
                    ((Room) RoomNo.get(Inp)).display();
                    fmt = true;
                } catch (NullPointerException e) {
                    System.out.println("Room number invalid try again...");
                }
            } while (!fmt);

            B1 = new Booking(((Room) RoomNo.get(Inp)).getRoomNo(), x.getDayOfYear(), y.getDayOfYear(), C1, ((Room) RoomNo.get(Inp)));
            List<Integer> dates = B1.getDuration(xLeap);
            Total = dates.size() * ((Room) RoomNo.get(Inp)).price(); //length of stay multiplied by price
            try {
                
                if (h.Overlap(B1, Bookings, xLeap, lBookings)) {
                    throw new DoubleBook("This room has been booked for sometime during your requested stay");
                }
                System.out.print("Total price\n£" + Total + "\nIs this ok?\t>>> ");
                lclEsc = AgreeDisagree.AgreeDisagree(s.next());

            } catch (DoubleBook e) {
                System.out.println(e.getMessage());
            }



        } while (!lclEsc);
        lBookings.add(B1);

        System.out.println("Your Booking ID\t"
                + B1.getCode());
    }

    @Override
    public boolean delete(List lBookings) {
        boolean confirm, x = false;
        System.out.print("""
                WARNING! YOU'RE ABOUT TO DELETE YOUR BOOKING. THIS CANNOT BE UNDONE!!!
                Is this ok?\t>>> """);
        confirm = AgreeDisagree.AgreeDisagree(s.next());

        if (confirm = true) {
            System.out.println("Input Unique booking ID\t>>> ");
            String Inp = s.next();

            try {
                for (int i = 0; i < lBookings.size(); i++) {
                    if (((Booking) lBookings.get(i)).getCode().equals(Inp)) {
                        ((Booking) lBookings.get(i)).show();
                        lBookings.remove(i);
                        x = true;
                    }
                }

            } catch (NullPointerException e) {
            }
        }

        return x;
    }

    /*
 * Admin Related methods
     */
    public static void admin(List rooms, List lBookings) { //Admin Menu

        System.out.println("///ADMIN HOTEL MENU///");
        boolean Exit = false;
        System.out.println("""
                Command List:
        List Rooms by:
            Room Number -> listroom
            Floor Number (Des) -> listfloor
        Create Room -> add
        Delete Room -> delete
        Report -> report
            """);

        do {
            HashMap<String, Room> RoomNo = new HashMap<>();
            int cont = rooms.size(); // repeated so Admin lists are update at same time
            for (int i = 0; i < cont; i++) {
                RoomNo.put(((Room) rooms.get(i)).getRoomNo(), (Room) rooms.get(i)); // creates Room Number list
            }
            System.out.print("Next Command\t>>> ");
            String Query = s.next();
            switch (Query.toLowerCase()) {
                case "add":
                    h.CrtRm();
                    break;
                case "delete":
                    h.DltRm(RoomNo);
                    break;
                case "listroom":
                    Collections.sort(rooms, new RmNumCmp());
                    h.listRm();
                    break;
                case "listfloor":
                    h.listfloor(RoomNo);
                    break;
                case "exit":
                    Exit = true;
                    break;
                case "report":
                    LocalDate date1 = null;
                    Exit = false;
                    do {
                        System.out.print("Input day (Format: YYYY-MM-DD)\t>>> ");
                        try {
                            date1 = LocalDate.parse(s.next());
                            Exit = true;
                        } catch (DateTimeParseException e) {
                            System.out.println("Exception occured: " + e.getMessage() + "\tTry again!");
                        }
                    } while (!Exit);
                    Exit = false;

                    h.Document(lBookings, RoomNo, date1);
                    break;
                case "sysexit":
                    System.exit(0);
                    break;
                default:
                    System.out.println("Unknown Command. Try again");
                    break;
            }

        } while (!Exit);
        System.out.println("You'll now be booted to the Customer Menu\n");
    }

    @Override
    public void CrtRm() {
        boolean locEsc = false, fnlDes;
        Room R1;
        boolean Kitchenette, z = false;
        String RmNo = null, type = null;
        int floor = 0, ocu = 0, Windows = 0, i = 0, bthrm = 0, LvnArea = 0;
        double balcony = 0.0;

        do {
            do { 
                try {
                    System.out.print("What type?\t>>> ");
                    type = s.next();
                    if (!"standard".equals(type.toLowerCase()) &&
                    !"deluxe".equals(type.toLowerCase()) &&
                    !"suite".equals(type.toLowerCase())) {
                        throw new InvalidRmTypException("Required room type not present");
                    }z=true;
                } catch (InvalidRmTypException e) {
                    System.out.println(e.getMessage());
                }
            } while (!z);
            z=false;


            do { //Room number check for inr
                System.out.print("Room Number:(2nd 2 digits)\t>>> ");
                try {
                    int room = s.nextInt();
                    RmNo = Integer.toString(room);
                    z = true;
                } catch (InputMismatchException e) {
                    System.out.println("Number must be entered");
                    s.next();
                }
            } while (!z);
            z = false;

            do { //Floor Number check for int
                System.out.print("Floor Number:\t>>> ");
                try {
                    floor = s.nextInt();
                    z = true;
                } catch (InputMismatchException e) {
                    System.out.println("Number must be entered");
                    s.next();
                }
            } while (!z);
            z = false;

            do { //Occupancy check for int
                System.out.print("Occupancy Number:\t>>>");
                try {
                    ocu = s.nextInt();
                    z = true;
                } catch (InputMismatchException e) {
                    System.out.println("Number must be entered");
                    s.next();
                }
            } while (!z);
            z = false;


            switch (type.toLowerCase()) { // Embedded Switch case
                case "std", "standard":
                    do {
                        do { //Window check for int
                            System.out.print("Window Number:\t>>>");
                            try {
                                Windows = s.nextInt();
                                z = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Number must be entered");
                                s.next();
                            }
                        } while (!z);

                        R1 = new Standard(RmNo, floor, ocu, Windows);
                        R1.display();

                        System.out.print("Is this ok?\t>>> ");
                        fnlDes = AgreeDisagree.AgreeDisagree(s.next());

                    } while (!fnlDes);
                    rooms.add(R1);
                    locEsc = true;
                    break;
                case "deluxe":
                    do {

                        do { //Balcony check for int
                            System.out.print("Balcony Size\t>>> ");
                            try {
                                balcony = s.nextDouble();
                                z = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Number must be entered");
                                s.next();
                            }
                        } while (!z);
                        z = false;

                        do {
                            System.out.print("Select View (1, 2, 3): \t1. Sea, 2. Landmark, 3. Mountain\n\t>>> ");
                            try {
                                i = s.nextInt();
                                if (i < 1 || i > 3) {
                                    throw new InputMismatchException();
                                }
                                z = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Input mismatch, try again");
                                s.next();
                            }
                        } while (!z);
                        R1 = new DeluxeRoom(RmNo, floor, ocu, balcony, i);
                        R1.display();
                        System.out.print("Is this ok?\t>>>");
                        fnlDes = AgreeDisagree.AgreeDisagree(s.next());

                    } while (!fnlDes);
                    rooms.add(R1);
                    locEsc = true;
                    break;
                case "suite":
                    do {

                        do {
                            System.out.print("Living Area Size\t>>> ");
                            try {

                                LvnArea = s.nextInt();
                                z = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Input mismatch, try again");
                                s.next();
                            }
                        } while (!z);
                        z = false;

                        do {
                            System.out.println("Bathroom Size\t>>> ");
                            try {

                                bthrm = s.nextInt();
                                z = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Input mismatch, try again");
                                s.next();
                            }
                        } while (!z);

                        System.out.print("Add optional kitchenette?\t>>> ");
                        Kitchenette = AgreeDisagree.AgreeDisagree(s.next());

                        R1 = new SuiteRoom(RmNo, floor, ocu, LvnArea, bthrm, Kitchenette);
                        R1.display();

                        System.out.println("Is this ok?\t>>> ");
                        fnlDes = AgreeDisagree.AgreeDisagree(s.next());

                    } while (!fnlDes);
                    rooms.add(R1);
                    locEsc = true;
                    break;
                case "esc":
                    locEsc = true;
                    break;
            }
        } while (!locEsc);

    }

    @Override
    public void DltRm(Map RoomNo) {
        boolean locEsc, fnlDes = false;
        String key;
        do {
            do {
                System.out.print("Enter room number\t>>> ");
                key = s.next();
                try {
                    ((Room) RoomNo.get(key)).display();
                    fnlDes = true;
                } catch (NullPointerException e) {
                    System.out.println("Error: Room not found! Try again!");
                }
            } while (!fnlDes);
            System.out.print("Is this the room you want to delete?\n>>> ");
            locEsc = AgreeDisagree.AgreeDisagree(s.next());

        } while (!locEsc);
        for (int i = 0; i + 1 < rooms.size(); i++) {
            if (RoomNo.get(key) == rooms.get(i)) { // searches array list for identical room
                rooms.remove(i);
                break;
            }
        }
        RoomNo.remove(key);

    }

    @Override
    public void listfloor(Map RoomNo) { //Admin, List all rooms by floor number
        /*
         * Splits all rooms into specific room types, organises them then puts them back together
         */
        Map<Integer, Room> floorNo = new HashMap<>();
        for (int i = 0; i < rooms.size(); i++) {
            floorNo.put(rooms.get(i).getfloor(), rooms.get(i));
        }
        List<Integer> floorNum = new ArrayList<>(floorNo.keySet());

        List<Room> DesRooms = new ArrayList<>();
        List<Room> Pile = rooms;
        List<Room> Pile2 = new ArrayList<>();
        int y = floorNum.getLast();//target floor

        int a = 0;
        do {
            Pile.removeAll(Pile2);
            y = y - a;

            List<Room> Buffer = new ArrayList<>();
            for (Room Pile1 : Pile) {
                if (Pile1.getfloor() == y) {
                    Buffer.add(Pile1);
                    Pile2.add(Pile1);
                }

            }
            a++;

            Collections.sort(Buffer, new RmNumRev());
            DesRooms.addAll(Buffer);

        } while (y >= 0);

        Collections.sort(Pile, new RmNumRev()); //adding ground floor last
        DesRooms.addAll(Pile);

        for (int i = 0; i < DesRooms.size(); i++) {
            DesRooms.get(i).display();
            System.out.println("------------------");
        }
    }

    @Override
    public void listRm() { //Admin, List all room by room number
        for (int i = 0; i < rooms.size() ; i++) {
            (rooms.get(i)).display();
            System.out.println("------------------");
        }
    }

    @Override
    public void Document(List lBookings, Map RoomNo, LocalDate date1) {
        //This part retrieves the local date and uses it as the text file name
        DateTimeFormatter fmtObj = DateTimeFormatter.ofPattern("ddMMyy"); //Declaring what format I want the date in (ddMMyy, mmssHH)
        String date = Today.format(fmtObj), rDate = date1.format(fmtObj);
        String Filename = "Report" + date + ".txt";
        PrintWriter writer = null;
        double Price = 0;
        int wDate = date1.getDayOfYear();
        Map<Integer, Room> Profit = new HashMap<>();
        boolean xLeap = LocalDate.now().isLeapYear();

        try {
            writer = new PrintWriter(Filename);
            writer.println("/////////" + rDate + "'s Bookings/////////");

            for (int i = 0; i < lBookings.size(); i++) {
                List<Integer> Duration = ((Booking) lBookings.get(i)).getDuration(xLeap);
                for (int j = 0; j < Duration.size(); j++) {
                    Profit.put(Duration.get(j), ((Booking) lBookings.get(i)).getRoom());
                }
                
            }

            if (Profit.get(wDate) == null) {
                writer.println("No Profit");
            } else {
                writer.println(Profit.get(wDate).info());
                Price = Price + ((Room) Profit.get(wDate)).price();
            }

        } catch (IOException e) {
            System.out.println("Error writing to file");

        } finally {

            writer.println("/////////Total Income:\t£" + Price);
            writer.close();

        }
        System.out.println(Filename + " has been saved!");

    }
}
