package User;

import java.util.ArrayList;
import java.util.List;
import room.Room;

public class Booking {

    private int arrival, departure;
    private String roomNo, uCode;
    private Customer C1;
    private List <Integer> Duration = new ArrayList<>();
    private int [] DurationArr;
    private Room room;


    public Booking(String roomNo, int arrival, int departure, Customer C1, Room room) {
        this.roomNo=roomNo;
        this.arrival = arrival;
        this.departure = departure;
        this.C1=C1;
        this.room=room;
        uCode=arrival+C1.toString().substring(10)+roomNo;
    }

    public int getArrival() {
        return arrival;
    }

    public int getDeparture() {
        return departure;
    }

    public String getCode() {
        
        return uCode;
    }

    public String getRoomNo() {
        return roomNo;
    }
    public Customer getCustomer(){//Might not be used
        return C1;
    }
    public Room getRoom(){
        return room;
    }
    public void show() { //mainly for testing
        System.out.println(C1.getName()+" has booked room "+roomNo+
        "\nArriving\t"+arrival+"\tDeparting\t"+departure+
        "\nBooking ID:\t"+uCode);
    }
    public List<Integer> getDuration(boolean xLeap)throws NullPointerException{ // returns the unique dates someone is staying 
        int x = arrival;
        int y = departure;

        for (int j = 0; x != y; j++) {
                
            if (x == 366 && xLeap == true) {
                x = 1;
                Duration.add(x);
            } else if (x == 365 && xLeap == false) {
                x = 1;
                Duration.add(x);
            } else {
                Duration.add(x);
                x++;
            }
        }
        return Duration;
    }
    public int [] getDurationArr(boolean xLeap)throws NullPointerException{ // returns the unique dates someone is staying 
        int x = arrival;
        int y = departure;

        for (int j = 0; x != y; j++) {
                
            if (x == 366 && xLeap == true) {
                x = 1;
                DurationArr[j]=x;
            } else if (x == 365 && xLeap == false) {
                x = 1;
                DurationArr[j]=x;
            } else {
                DurationArr[j]=x;
                x++;
            }
        }
        return DurationArr;
    }
}
