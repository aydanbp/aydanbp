
import User.Booking;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import room.Room;


public class Report {

    public static void Document(List lBookings, LocalDate Today, Map RoomNo, LocalDate date1){
{
        //This part retrieves the local date and uses it as the CSV name
        DateTimeFormatter fmtObj = DateTimeFormatter.ofPattern("ddMMyy"); //Declaring what format I want the date in (ddMMyy, mmssHH)
        String date = Today.format(fmtObj), rDate = date1.format(fmtObj);
        String Filename = "Report"+date+".txt";
        PrintWriter writer = null;
        double Price=0;
        int wDate = date1.getDayOfYear();
        Map<Integer, Room> Profit = new HashMap<>();
        List <Integer> Duration = new ArrayList<>();
        boolean xLeap = LocalDate.now().isLeapYear();
        
        
        try {
            writer = new PrintWriter(Filename);
            writer.println("/////////"+rDate+"'s Bookings/////////");

            for (int i = 0; i < lBookings.size(); i++) {
                Duration = ((Booking) lBookings.get(i)).getDuration(xLeap);
                for (int j = 0; j < Duration.size()-1; j++) {
                    Profit.put(Duration.get(j), ((Booking) lBookings.get(i)).getRoom());
                }
                
                
            }

            if (Profit.get(wDate)==null) {
                writer.println("No Profit");
                
            }
            else{
                writer.println(Profit.get(wDate).info());
                Price = Price + ((Room) Profit.get(wDate)).price();
            }
            

        } catch (IOException e) {
            System.out.println("Error writing to file");
        
        }
        finally{

            writer.println("/////////Total Income:\t£"+Price);
                writer.close();
            
            
        }
        System.out.println(Filename+" has been saved!");
        
    }}}

