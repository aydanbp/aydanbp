/*
The purpose of this program is to create a text based interface where a customer can book different types of rooms 
and an admin can create and destroy rooms
VERSION 0.2
 */
import User.Customer;
import room.DeluxeRoom;
import room.Standard;
import room.Room;
import java.util.Scanner;

public class HotelMenu {

    public static void main(String[] args) {
        Boolean validIp = false;
        System.out.println("Welcome to HOTEL W2087342 \tPlease input your first name\n");
        Scanner s = new Scanner(System.in);
        String Name = s.next();
        System.out.println("Thank you. Please input your Second name\n");
        String Name2 = s.next();
        System.out.println("Input the year of your birth\n");
        int bYear = s.nextInt();
        Customer i = new Customer();
        i.Customer(Name, Name2, bYear, bYear);
        Standard R = new Standard();
        R.Standard(Name, bYear, Name, Name);
        Room Rm = new Room();
        

    }
}
