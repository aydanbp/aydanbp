/*
The purpose of this program is to create a text based interface where a customer can book different types of rooms 
and an admin can create and destroy rooms
VERSION 0.3
 */
import User.Customer;
import java.util.Scanner;
import room.DeluxeRoom;
import room.Standard;

public class HotelMenu {

    public static void main(String[] args) {
        int bYear=0;
        boolean validIp=false;
        System.out.println("Welcome to HOTEL W2087342 \tPlease input your first name\n");
        Scanner s = new Scanner(System.in);
        String Name = s.next();
        System.out.println("Thank you. Please input your Second name");
        String Name2 = s.next();
        System.out.println("Input the year of your birth");
        do {    // Loop until a number is entered
        try {
            bYear = Integer.parseInt(s.next());
            validIp = true;
        } catch (NumberFormatException e) {     //NOTE: CATCHING ALL EXCEOTIONS = LOWER MARKS
            System.out.println("Oops, this error occured. Contact your coder with this error:\t"+e);
        }
    }while(!validIp);
        Customer i = new Customer();
        i.Customer(Name, Name2, bYear, bYear);
        Standard R = new Standard(Name2, bYear, bYear, bYear);
        DeluxeRoom RD = new DeluxeRoom(Name2, bYear, bYear, bYear, bYear);

    }
}
