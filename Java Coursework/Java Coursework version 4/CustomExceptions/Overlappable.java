package CustomExceptions;

import User.Booking;
import java.util.Map;

/*
 * This is in custom exceptions because it's directly linked to the Double book exception
 */
public interface Overlappable{ 
     
    
    public Boolean Overlap (Booking B1, Map Bookings, boolean xLeap, Map desiredRooms);
}