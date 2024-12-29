import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class Test {
    // Generating Rooms for future use
    public static void main(String[] args) throws IOException {
        int [] dates = new int[999];
        Scanner s = new Scanner(System.in);
        String x, y;
        int date1, date2, z;
        x = "2024-12-31";
        y = "2025-01-24";
        
        date1 = LocalDate.parse(x).getDayOfYear();
        date2 = LocalDate.parse(y).getDayOfYear();
        boolean xLeap = LocalDate.parse(x).isLeapYear();
        System.out.println(x);
        System.out.println(y);
        for (int i = 0; date1 != date2; i++) {
            
            if (date1 == 367 && xLeap == true){
                date1 = 1;
                dates[i] = date1;
                date1++;
            }
            else if (date1 == 366 && xLeap == false) {
                date1 = 1;
                dates[i] = date1;
                date1++;
            }
            else{
                dates[i] = date1;
                date1++;
            }
        }

        for (int i = 0; i <= (dates.length-1); i++) {
            if (dates[i] != 0){
                System.out.println(dates[i]);
            }
        }
    }
}