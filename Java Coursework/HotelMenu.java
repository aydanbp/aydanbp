
import User.Admin;
import User.Customer;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static room.RmGen.RmGen;
import room.Room;


/*
The purpose of this program is to create a text based interface where a customer can book different types of rooms 
and an admin can create and destroy rooms
VERSION 0.6
 */
public class HotelMenu {

    public static void main(String[] args) throws IOException {
        List<Room> rooms = RmGen();
        List<Customer> customers = new ArrayList<Customer>();

        System.out.println("///CUSTOMER HOTEL MENU///");
        Scanner s = new Scanner(System.in);
        String Query; // This is used as an input
        String[][] bookID = new String[367][999]; // 2D Array used to track if a room is booked, 1st represents room
                                                  // number, 2nd represents date
        boolean Exit = false;

        int nowDoY = LocalDate.now().getDayOfYear();
        Calendar dt = Calendar.getInstance();
        System.out.println("Welcome Customer, please input first name:\t");
        String Name = s.next();
        System.out.println("Thank you, now your surname:\t");
        String sName = s.next();
        System.out.println("Date of birth (Format YYYY-MM-DD):\t");
        String DoB = s.next();
        System.out.println("Thank you. What room would you like?");
        Customer C1 = new Customer(Name, sName, DoB);

        System.out.println("""
                    Command List:
                    List Rooms by:
                        date (ASC) -> time
                        day, occupancy and type -> type
                    Room booking -> book
                    Delete Booking -> delete book
                """);

        do { // Rooms will be updated every cycle
            Collections.sort(rooms, new PriceCompare());
            HashMap<String, Room> RoomNo = new HashMap<>();
            int cont = rooms.size();
            for (int i = 1; i < cont; i++) {
                RoomNo.put(rooms.get(i - 1).getRoomNo(), rooms.get(i - 1)); // creates Room Number list
            }

            System.out.println("Next Command\n>>>");
            Query = s.next();
            int day = 0;
            boolean fmt = false;
            boolean lclEsc = false;
            String bkID, Inp;
            switch (Query.toLowerCase()) {
                case "time": // List all rooms avalible on specific days starting with lowest price
                    Customer.list(rooms, bookID);
                    System.out.print("Next Command:\t///");
                    break;
                case "octp": // List all rooms avalible on specific days, with specific types and occupancy
                    Customer.listOucType(rooms, bookID);
                    System.out.print("Next Command:\t///");
                    break;
                case "book":// book room on specific day with room number, generates booking ID
                    Customer.booking(RoomNo, C1, bookID);

                    System.out.print("Next Command:\t///");
                    break;
                case "deletebook":// delete room booking via booking ID
                    int x = Customer.delete(bookID);
                    if (x != 0) {
                        System.out.println("your booking has been deleted");
                        System.out.println(x + " bookings deleted");
                    } else {
                        System.out.println(x + " bookings deleted");
                        System.out.println("No booking was found with that name");
                    }
                    break;
                case "exit":
                    Exit = true;
                    System.out.println("Goodbye");
                    break;
                case "admin":
                    // rooms.addAll(admin(RoomNo, rooms));
                    admin(RoomNo, rooms);// Activates admin menu and gives the Room map & Rooms list
                    break;
                default:
                    System.out.println("Unknown value, enter again");
                    break;
            }
        } while (!Exit);
        customers.add(C1);
        System.out.println("Recipt saved here:");
    }

    public static void admin(Map RoomNo, List rooms) throws IOException {
        Scanner s = new Scanner(System.in);
        System.out.println("///ADMIN MENU///");
        System.out.println("Welcome to the admin menu");
        boolean AdEsc = false;
        do {
            System.out.println("Please enter your next ADMIN command:\t>>>");
            String AdQu = s.next();
            switch (AdQu) {
                case "add": // Add any room type
                    Admin.CrtRm(RoomNo, rooms);
                    break;
                case "delete": // delete any room type
                Admin.DltRm(RoomNo, rooms);
                    break;
                case "listroom": // List rooms by room number, asc
                Collections.sort(rooms, new RmNumCmp());
                Admin.list(rooms);
                break;
                case "listfloor": // list rooms by floor, top floor 1st
                Collections.sort(rooms, new RmNumRev());
                    Admin.listfloor(RoomNo, rooms);
                    break;
                case "report": // Generate a report
                    Report.ReportW();
                    break;
                case "exit": // Closes entire system
                    System.out.println("System Exit. Goodbye");
                    System.exit(0);
                case "esc":
                    AdEsc = true;
                    break;
                default:
                    System.out.println("command not found");
                    break;
            }
        } while (!AdEsc);
        System.out.println("You will now be booted to the Customer Menu\n");
    }

}
