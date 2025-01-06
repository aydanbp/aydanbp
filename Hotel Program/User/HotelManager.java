package User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface HotelManager  {

    void CrtRm();

    void DltRm(Map RoomNo);
    
    void listRm();

    void listfloor(Map RoomNo);

    void Document(List lBookings, Map RoomNo, LocalDate date1);
    
}
