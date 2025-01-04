package User;

import java.util.List;
import java.util.Map;

public interface HotelCustomer{

    void list(Map Bookings, List lBookings, Map RoomNo);

    void listOucType(Map desiredRooms, Map Bookings, List lBookings, Map RoomNo);

    void booking(Booking B1, Customer C1, Map RoomNo, Map Bookings, List lBookings);

    boolean delete(List lBookings);

}
