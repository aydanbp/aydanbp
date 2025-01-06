package Sorters;

import java.util.Comparator;
import room.Room;

public class RmNumRev implements Comparator<Room>{
    @Override
    public int compare(Room o1, Room o2) {
        if (Integer.parseInt(o1.getRoomNo()) > Integer.parseInt(o2.getRoomNo()))
            return -1;
        else if (o1.price() > o2.price())
            return 1;
        else
            return 0;
    }

}