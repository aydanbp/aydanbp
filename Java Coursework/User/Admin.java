package User;

import java.util.List;
import java.util.Map;
import java.util.Scanner;
import room.DeluxeRoom;
import room.Room;
import room.Standard;
import room.SuiteRoom;

public class Admin extends User {
    static boolean AdEsc = false, locEsc = false, fnlDes = false;
    static Scanner s = new Scanner(System.in);
    int floor;

    public Admin() {

    }

    public static void CrtRm(Map RoomNo, List rooms) { // Creates Room
        Room R1;
        boolean Kitchenette;
        String RmNo;
        int floor, ocu, Windows, i, bthrm, LvnArea;
        double balcony;
        System.out.println("What type?\t>>>");
        do {
            String type = s.next();
            switch (type.toLowerCase()) { // Embedded Switch case
                case "std", "standard":
                    do {
                        System.out.println("Room Number:(2 digits)\t");
                        RmNo = s.next();
                        System.out.println("Floor Number:\t");
                        floor = s.nextInt();
                        System.out.println("Occupancy Number:\t");
                        ocu = s.nextInt();
                        System.out.println("Window Number:\t");
                        Windows = s.nextInt();
                        R1 = new Standard(RmNo, floor, ocu, Windows);
                        R1.display();
                        System.out.println("Is this ok?");
                        fnlDes = AgreeDisagree(s.next());
                    } while (!fnlDes);
                    rooms.add(R1);
                    locEsc = true;
                    break;
                case "deluxe":
                    do {
                        System.out.println("Room Number:(2 digits)\t");
                        RmNo = s.next();
                        System.out.println("Floor Number:\t");
                        floor = s.nextInt();
                        System.out.println("Occupancy Number:\t");
                        ocu = s.nextInt();
                        System.out.println("Balcony Size");
                        balcony = s.nextDouble();
                        System.out.println("Select View (1, 2, 3): \t1. Sea, 2. Landmark, 3. Mountain");
                        i = s.nextInt();
                        R1 = new DeluxeRoom(RmNo, floor, ocu, balcony, i);
                        System.out.println("Is this ok?");
                        fnlDes = AgreeDisagree(s.next());
                    } while (!fnlDes);
                    rooms.add(R1);
                    locEsc = true;
                    break;
                case "suite":
                    do {
                        System.out.println("Room Number:(2 digits)\t");
                        RmNo = s.next();
                        System.out.println("Floor Number:\t");
                        floor = s.nextInt();
                        System.out.println("Occupancy Number:\t");
                        ocu = s.nextInt();
                        System.out.println("Living Area Size");
                        LvnArea = s.nextInt();
                        System.out.println("Bathroom Size");
                        bthrm = s.nextInt();
                        System.out.println("Add optional kitchenette?");
                        Kitchenette = AgreeDisagree(s.next());
                        R1 = new SuiteRoom(RmNo, floor, ocu, LvnArea, bthrm, Kitchenette);
                        R1.display();
                        System.out.println("Is this ok?");
                        fnlDes = AgreeDisagree(s.next());
                    } while (!fnlDes);
                    rooms.add(R1);
                    locEsc = true;
                    break;
                case "esc":
                    locEsc = true;
                default:
                    System.out.println("Try again");
            }
        } while (!locEsc);
    }

    public static void DltRm(Map RoomNo, List rooms) { // Deletes Room
        String key;
        do {
            System.out.println("Enter room number");
            key = s.next();
            do {
                try {
                    ((Room) RoomNo.get(key)).display();
                    fnlDes = true;
                } catch (Exception e) {
                    System.out.println("Error: Room not found! Try again!");
                    key = s.next();
                }
            } while (!fnlDes);
            System.out.println("Is this the room you want to delete?\n>>>");
            locEsc = AgreeDisagree(s.next());
        } while (!locEsc);
        for (int i = 0; i+1 < rooms.size(); i++) {
            if (RoomNo.get(key) == rooms.get(i)){
                rooms.remove(i);
                break;
            }
        }
        RoomNo.remove(key);
    }

    public static void list(List rooms) {
       for (int i = 0; i < rooms.size()-1; i++) {
           ((Room) rooms.get(i)).display();
       } 
    }

    public static void listfloor(Map RoomNo, List rooms) {
        for (int i = 0; i < rooms.size()-1; i++) {
            ((Room) rooms.get(i)).display();
        } 
    }

}
