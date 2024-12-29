package room;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
public class RmGen {
public static List RmGen(){
    Random r = new Random();
    String[] RuNo = { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09" };
    List<Room> rooms = new ArrayList<Room>();// For all rooms
    int i, swch;
    for (i = 0; i < 10; i++) {// Ground floor
        Room S1 = new Standard(RuNo[i], 0, r.nextInt(1, 5), r.nextInt(1, 5));
        rooms.add(S1); // adding to regular room list
    }

    for (i = 0; i < 10; i++) {// 1st floor
        swch = r.nextInt(1,3);
        switch (swch) {
            case 1:
                Room S1 = new Standard(RuNo[i], 1, r.nextInt(1, 5), r.nextInt(1, 5));
                rooms.add(S1); // adding to regular map
                break;
            case 2:

                Room S2 = new DeluxeRoom(RuNo[i], 1, r.nextInt(1, 5), r.nextInt(1, 20), r.nextInt(0, 2));
                rooms.add(S2); // adding to regular map
                break;

            case 3:
                boolean kt = r.nextBoolean();
                int sz = r.nextInt(1, 20);
                Room S3 = new SuiteRoom(RuNo[i], 1, r.nextInt(1, 5), sz, r.nextInt(1, 2), kt);
                rooms.add(S3); // adding to regular map
                break;

        }
    }
    for (i = 0; i < 10; i++) {// 2nd floor
        swch = r.nextInt(1, 3);
        switch (swch) {
            case 1:
                Room S2 = new DeluxeRoom(RuNo[i], 2, r.nextInt(1, 5), r.nextInt(1, 20), r.nextInt(0, 2));
                rooms.add(S2); // adding to regular map
                break;
            case 2:
                boolean kt = r.nextBoolean();
                int sz = r.nextInt(1, 20);
                Room S3 = new SuiteRoom(RuNo[i], 2, r.nextInt(1, 5), sz, r.nextInt(1, 2), kt);
                rooms.add(S3); // adding to regular map
                break;
        }
    }

return rooms;
}
}


