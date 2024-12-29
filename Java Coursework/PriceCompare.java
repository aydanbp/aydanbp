
import java.util.Comparator;
import room.Room;

public class PriceCompare implements Comparator<Room>{
    @Override
    public int compare(Room o1, Room o2) {
        if (o1.price() < o2.price())
            return -1;
        else if (o1.price() > o2.price())
            return 1;
        else
            return 0;
    }

}