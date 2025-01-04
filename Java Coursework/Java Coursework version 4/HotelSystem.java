
import CustomExceptions.DoubleBook;
import CustomExceptions.InvalidDateTypeException;
import CustomExceptions.InvalidRmTypException;
import CustomExceptions.OqPiException;
import CustomExceptions.Overlappable;
import Sorters.PriceCompare;
import Sorters.RmNumCmp;
import Sorters.RmNumRev;
import User.Customer;
import User.HotelCustomer;
import User.HotelManager;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import User.Booking;
import room.DeluxeRoom;
import static room.RmGen.RmGen;
import room.Room;
import room.Standard;
import room.SuiteRoom;

/*
The purpose of this program is to create a text based interface where a customer can book different types of rooms 
and an admin can create and destroy rooms
VERSION 1.0a
 */
public class HotelSystem implements HotelCustomer, HotelManager, Overlappable, AgreeDisagree {

    static HotelSystem h = new HotelSystem();
    static List<Room> rooms = RmGen();
    static Scanner s = new Scanner(System.in);
    static LocalDate Today = LocalDate.now();

    public static void main(String[] args){
        mainMenu(args);
    }
    public static void mainMenu(String[] args) { // main menu
        
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
        HashMap<String, Room> RoomNo = new HashMap<>();
        Customer C1 = null;
        LocalDate DoB = null;
        Booking B1 = null;
        HashMap <String, Booking> Bookings = new HashMap<>();
        List <Booking> lBookings = new ArrayList<Booking>();
        boolean Exit = false;
        
        do {
            HashMap<Integer, Room> desiredRooms = new HashMap<>(); //Room map that uses Room number for keys
            Collections.sort(rooms, new PriceCompare());
            int cont = rooms.size(), cont2 = lBookings.size();
            for (int i = 1; i < cont; i++) {
                RoomNo.put(rooms.get(i - 1).getRoomNo(), rooms.get(i - 1)); // creates Room Number list
            }
            for (int i = 1; i < cont2; i++){
                Bookings.put(lBookings.get(i-1).getRoomNo(),lBookings.get(i-1)); //Creates booking list
            }
            System.out.print("Next Command\t>>> ");
            String Query = s.next();
            switch (Query.toLowerCase()) {

                case "admin":
                    admin(rooms, RoomNo, lBookings);
                    break;
                case "time":
                    h.list(Bookings, lBookings, RoomNo);
                    break;
                case "type":
                    h.listOucType(desiredRooms, Bookings, lBookings, RoomNo);
                    break;
                case "book":
                    System.out.print("Please input first & Last name name:\t>>> ");
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
                    System.out.print("Phone Number:\t>>> ");
                    String pNum = s.next();
                    System.out.print("Email:\t>>> ");
                    String eMl = s.next();
                    System.out.print("Thank you.");
                    C1 = new Customer(Name, DoB, pNum, eMl);
                    h.booking(B1,C1, RoomNo, Bookings, lBookings);
                    break;
                case "delete":
                    boolean x = h.delete(lBookings);
                    if (!x) {
                        System.out.println("Booking deletion unsucessful");
                        
                    }else{
                        System.out.println("Booking deletion successful!");
                    }

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
    public Boolean Overlap(Booking B1, Map Bookings, boolean xLeap, Map desiredRooms) throws NullPointerException {
        Boolean x = false;
        List <Booking> Overlaps = new ArrayList<>();
        List <Integer> dates1 = new ArrayList<>();
        List <Integer> dates2 = new ArrayList<>();
        
        int Strike = 0, arrival, departure, i, cont = Bookings.size() ;
        String RmNum = B1.getRoomNo();
        
        dates2.addAll(((Booking) Bookings.get(RmNum)).getDuration(xLeap));
        

        /*
         * Senario: Customer A has booked Monday to Wednesday
         * Customer C has booked Friday to Sunday
         * Customer B can only book Wednesday to Friday but is unaware of A&C
         * Remember this code is checking is there are two bookings
         */


        dates1 = B1.getDuration(xLeap);
        arrival = B1.getArrival();
        departure = B1.getDeparture();
        System.out.println("Size of possible overlaps "+dates1.size());
        System.out.println("Size of possible overlaps "+dates2.size());


            for (i = 0; i < dates1.size()-1; i++) { // if a date matches another date then overlap will be true

                for (int j = 0; j < dates2.size()-1; j++) {
                    System.out.println(dates1.get(i)+"-"+dates2.get(j));
                    if (Objects.equals(dates1.get(i), dates2.get(j))){
                        x=true;
                        
                    }

                }
                
            }
            System.out.println("Value X came back"+x);

                return x;

                
    }

    @Override
    public void list(Map Bookings, List lBookings, Map RoomNo) {
        int day = 0, Avil=0;
        Map <String, Booking> notAv = new HashMap<>();
        Map <String, Room> DesRooms = RoomNo;
        List < Room> Av = new ArrayList<>();
        String [] noAv = new String[367];
        String [] Avl = new String[367];
        boolean fmt = false;
        
        do {
            System.out.print("Input day (Format YYYY-MM-DD): \t");
            try {
                day = LocalDate.parse(s.next()).getDayOfYear();
                fmt = true;
            } catch (DateTimeParseException e) {
                System.out.println("Exception occured:\tInvalid date given\tTry again!");
            }
        } while (!fmt);

        //if the arrival dat is today don't book it

        for (int i = 0; i < lBookings.size()-1; i++) { // list of unavalible rooms
                if (((Booking) lBookings.get(i)).getArrival() == day) {
                    noAv[i] = ((Booking) lBookings.get(i)).getRoomNo();
                }
        }
        for (String key : noAv) {
            DesRooms.remove(key);
        }
        Av.addAll(DesRooms.values());
    System.out.println("Rooms unavalible\t"+noAv);
    
    

    System.out.println(Av.size()+" rooms out of "+rooms.size()); 
        
    for (int i = 0; i < Av.size() - 1; i++) {
        ((Room) Av.get(i)).display();
    }
        


    }

    @Override
    public void listOucType(Map desiredRooms, Map Bookings, List lBookings, Map RoomNo) {
        List<Room> desRooms = new ArrayList<>();
        List < Room> Av = new ArrayList<>();
        int day = 0;
        boolean fmt = false, Exit = false;
        String type = null;
        int[] Des;
        String[] noAv = new String[999];
        Map <String, Room> DesRooms = RoomNo;
        int cont = rooms.size(), ocu = 0;
        for (int i = 0; i < lBookings.size()-1; i++) { // list of unavalible rooms
            if (((Booking) lBookings.get(i)).getArrival() == day) {
                
                    noAv[i] = ((Booking) lBookings.get(i)).getRoomNo();
            }
    }        for (String key : noAv) {
        DesRooms.remove(key);
    }
    Av.addAll(DesRooms.values());

        do {
            System.out.print("Input day (Format YYYY-MM-DD): \t");
            try {
                day = LocalDate.parse(s.next()).getDayOfYear();
                fmt = true;
            } catch (DateTimeParseException e) {
                System.out.println("Exception occured:\tInvalid date given\tTry again!");
            }
        } while (!fmt);
        fmt = false;
        do {// same day loop
            do {
                System.out.print("Room type\t>>> ");
                try {
                    type = s.next();
                    if (!type.toLowerCase().contains("standard")
                            || !type.toLowerCase().contains("deluxe")
                            || !type.toLowerCase().contains("suite")) {
                        throw new InvalidRmTypException("Invalid room type selected");
                    }
                } catch (InvalidRmTypException e) {
                }
            } while (!fmt);
            fmt = false;
            for (int i = 1; i < cont; i++) { 
                if ((Av.get(i - 1)).type() == type) {
                    desiredRooms.put((Av.get(i - 1)).getOcup(), Av.get(i - 1));
                }
            }
            System.out.print("Occupancy\t>>> ");
            do {
                try {
                    ocu = s.nextInt();
                    fmt = true;
                } catch (InputMismatchException e) {
                    System.out.println("This isn't number... Try again!");
                    System.out.print("Occupancy\t>>> ");
                }
            } while (!fmt);
            fmt = false;
            try {
                desRooms.add((Room) desiredRooms.get(ocu));// pass back to list so can be checked against booking 1-1

                if (desRooms.size() == 0) {
                    throw new OqPiException("Our system doesn't contain that room type with that ocupancy");
                }
                for (int i = 1; i < desRooms.size(); i++) {// only prints if no booking ID
                        (desRooms.get(i - 1)).display();
                    
                }
            } catch (OqPiException e) {
                System.out.println(e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("This room, date & occupancy don't match our systems");
            }
            System.out.println("Would you like to enter a different occupancy\t>>> ");
            Exit = AgreeDisagree.AgreeDisagree(s.next());
        } while (!Exit);
    }

    @Override
    public void booking(Booking B1, Customer C1, Map RoomNo, Map Bookings, List lBookings) {
        int[] dates = new int[367];
        String uCode, Inp = null;
        boolean xLeap = false, fmt = false, lclEsc = false;
        int day = 0, Nights = 0, dif, date1, date2;
        LocalDate x = null, y = null;
        int i;
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
                    System.out.println("Exception occured: \t" + e.getMessage() + "\tTry again!");
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
                    System.out.println("This is not a valid date");
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
            uCode = C1.toString().substring(10) + RoomNo.get(Inp).toString().substring(10);
            for (i = 0; day != Nights; i++) { //Using day variable to calculate price
                if (day == 366 && xLeap == true) {
                    day = 1;
                    dates[i] = day;
                } else if (day == 365 && xLeap == false) {
                    day = 1;
                    dates[i] = day;
                } else {
                    dates[i] = day;
                    day++;
                }
            }
            double Total = ((Room) RoomNo.get(Inp)).price() * day; //Calculated price
            day = x.getDayOfYear(); //Changing day back to original value
            

                B1 = new Booking(RoomNo.get(Inp).toString(), x.getDayOfYear(), y.getDayOfYear(), uCode, C1, ((Room) RoomNo.get(Inp)));
                try {
                    if (h.Overlap(B1, Bookings, xLeap, RoomNo)) {
                        throw new DoubleBook("This room has been booked for sometime during your requested stay");
                    }
                    else{
                        System.out.println("Total price\n£" + Total + "\nIs this ok?\t>>> ");
                        lclEsc = AgreeDisagree.AgreeDisagree(s.next());
                    }

                } catch (DoubleBook e) {
                    System.out.println(e.getMessage());

                }catch (NullPointerException ex){
                    //Will ignore Null pointer exceptions becasue first time a room is booked will allways throw exception
                    System.out.println("Total price\n£" + Total + "\nIs this ok?\t>>> ");
                    lclEsc = AgreeDisagree.AgreeDisagree(s.next());
                }
            

        } while (!lclEsc);
        lBookings.add(B1);
        

        System.out.println("Your Booking ID\t"
                + B1.getCode());
    }

    @Override
    public boolean delete(List lBookings) {
        boolean confirm, z = false, x=false;
        String Inp;
        int i, j = 0;
        System.out.print("""
                         WARNING! YOU'RE ABOUT TO DELETE YOUR BOOKING. THIS CANNOT BE UNDONE!!!
                         Is this ok?\t>>> """);
        confirm = AgreeDisagree.AgreeDisagree(s.next());
        if (confirm = true) {
            System.out.println("Input Unique booking ID\t>>> ");
            Inp = s.next();
            System.out.println("Room Number\t>>> ");
            do {

                try {
                    j = s.nextInt();
                    z=true;
                } catch (InputMismatchException e) {
                    System.out.println("That's not a number, Try again!");
                    System.out.println("Room Number\t>>> ");
                }
            } while (!z);
            try {
                for ( i = 0; i < lBookings.size(); i++) {
                    if (((Booking) lBookings.get(i)).getCode().equals(Inp)){
                        ((Booking) lBookings.get(i)).show();
                        lBookings.remove(i);
                        x=true;
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
    public static void admin(List rooms, Map RoomNo, List lBookings) { //Admin Menu
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

                    Report.Document(lBookings, Today, RoomNo, date1);
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
        boolean AdEsc = false, locEsc = false, fnlDes = false;
        Room R1;
        boolean Kitchenette, z = false;
        String RmNo = null;
        int floor = 0, ocu = 0, Windows = 0, i = 0, bthrm = 0, LvnArea = 0;
        double balcony = 0.0;
        System.out.println("What type?\t>>> ");
        do {
            String type = s.next();
            switch (type.toLowerCase()) { // Embedded Switch case
                case "std", "standard":
                    do {

                        System.out.println("Room Number:(2nd 2 digits)\t");
                        RmNo = s.next();
                        System.out.println("Floor Number:\t");
                        do { //Floor Number check for int

                            try {
                                floor = s.nextInt();
                                z = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Number must be entered");
                                System.out.println("Floor Number:\t");
                            }
                        } while (!z);
                        z = false;
                        System.out.println("Occupancy Number:\t");
                        do { //Occupancy check for int

                            try {
                                ocu = s.nextInt();
                                z = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Number must be entered");
                                System.out.println("Occupancy Number:\t");
                            }
                        } while (!z);
                        z = false;
                        System.out.println("Window Number:\t");
                        do { //Window check for int

                            try {
                                Windows = s.nextInt();
                                z = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Number must be entered");
                                System.out.println("Window Number:\t");
                            }
                        } while (!z);

                        R1 = new Standard(RmNo, floor, ocu, Windows);
                        R1.display();
                        System.out.println("Is this ok?");
                        fnlDes = AgreeDisagree.AgreeDisagree(s.next());
                    } while (!fnlDes);
                    rooms.add(R1);
                    locEsc = true;
                    break;
                case "deluxe":
                    do {
                        System.out.println("Room Number:(2 digits)\t");
                        RmNo = s.next();
                        System.out.println("Floor Number:\t");
                        do { //Floor Number check for int
                            try {
                                floor = s.nextInt();
                                z = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Number must be entered");
                                System.out.println("Floor Number:\t");
                            }
                        } while (!z);
                        z = false;

                        System.out.println("Occupancy Number:\t");
                        do { //Occupancy check for int
                            try {
                                ocu = s.nextInt();
                                z = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Number must be entered");
                                System.out.println("Occupancy Number:\t");
                            }
                        } while (!z);
                        z = false;

                        System.out.println("Balcony Size");
                        do { //Balcony check for int
                            try {
                                balcony = s.nextDouble();
                                z = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Number must be entered");
                                System.out.println("Balcony Size");
                            }
                        } while (!z);
                        z = false;

                        System.out.println("Select View (1, 2, 3): \t1. Sea, 2. Landmark, 3. Mountain");
                        do {
                            try {
                                i = s.nextInt();
                                if (i < 1 || i > 3) {
                                    throw new InputMismatchException();
                                }
                                z = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Input mismatch, try again");
                                System.out.println("Select View (1, 2, 3): \t1. Sea, 2. Landmark, 3. Mountain");
                            }
                        } while (!z);

                        R1 = new DeluxeRoom(RmNo, floor, ocu, balcony, i);
                        System.out.println("Is this ok?\n>>>");
                        fnlDes = AgreeDisagree.AgreeDisagree(s.next());
                    } while (!fnlDes);
                    rooms.add(R1);
                    locEsc = true;
                    break;
                case "suite":
                    do {
                        System.out.println("Room Number:(2 digits)\t>>> ");
                        RmNo = s.next();
                        System.out.println("Floor Number\t>>> ");
                        do {
                            try {
                                floor = s.nextInt();
                                z = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Input mismatch, try again");
                                System.out.println("Floor Number\t>>> ");
                            }
                        } while (!z);
                        z = false;

                        System.out.println("Occupancy Number\t>>> ");
                        do {
                            try {
                                ocu = s.nextInt();
                                z = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Input mismatch, try again");
                                System.out.println("Occupancy Number\t>>> ");
                            }
                        } while (!z);
                        z = false;

                        System.out.println("Living Area Size\t>>> ");
                        do {
                            try {

                                LvnArea = s.nextInt();
                                z = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Input mismatch, try again");
                                System.out.println("Living Area Size\t>>> ");
                            }
                        } while (!z);
                        z = false;

                        System.out.println("Bathroom Size\t>>> ");
                        do {
                            try {

                                bthrm = s.nextInt();
                                z = true;
                            } catch (InputMismatchException e) {
                                System.out.println("Input mismatch, try again");
                                System.out.println("Bathroom Size\t>>> ");
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
                default:
                    System.out.println("Sorry, not a valid input");
            }
        } while (!locEsc);

    }

    @Override
    public void DltRm(Map RoomNo) {
        boolean locEsc = false, fnlDes = false;
        String key;
        do {
            System.out.println("Enter room number\t>>> ");
            key = s.next();
            do {
                try {
                    ((Room) RoomNo.get(key)).display();
                    fnlDes = true;
                } catch (NullPointerException e) {
                    System.out.println("Error: Room not found! Try again!");
                    key = s.next();
                }
            } while (!fnlDes);
            System.out.println("Is this the room you want to delete?\n>>> ");
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
        Map <Integer, Room> floorNo = new HashMap<>();
        for (int i = 0; i < rooms.size()-1; i++) {
            floorNo.put(rooms.get(i).getfloor(), rooms.get(i));
        }
        List<Integer> floorNum = new ArrayList<>(floorNo.keySet());

        List<Room> DesRooms = new ArrayList<>();
        List<Room> Pile = rooms;
        List<Room> Pile2 = new ArrayList<>();
        int y = floorNum.getLast();//target floor
        
        int a = 0;
        boolean esc = false;
        do {
            Pile.removeAll(Pile2);
            System.out.println(Pile.size());
            y=y-a;
                
            
            List<Room> Buffer = new ArrayList<>();
            for (Room Pile1 : Pile) {
                if (Pile1.getfloor() == y){
                    Buffer.add(Pile1);
                    Pile2.add(Pile1);
                }
                
            }a++;
            
            Collections.sort(Buffer, new RmNumRev());
            DesRooms.addAll(Buffer);
            
        
        } while (y<0);

        Collections.sort(Pile, new RmNumRev()); //adding ground floor last
        DesRooms.addAll(Pile);


        for (int i = 0; i < DesRooms.size(); i++) {
            DesRooms.get(i).display();
        }
    }

    @Override
    public void listRm() { //Admin, List all room by room number
        for (int i = 0; i < rooms.size() - 1; i++) {
            (rooms.get(i)).display();
        }
    }
}
