import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Report {
    public static boolean ReportW()throws IOException{
        //This part retrieves the local date and uses it as the CSV name
        DateTimeFormatter fmtObj = DateTimeFormatter.ofPattern("ddMMyy"); //Declaring what format I want the date in (ddMMyy, mmssHH)
        LocalDate rawDt = LocalDate.now();
        String fmtDt = rawDt.format(fmtObj);
        System.out.println(fmtDt);
        String Filename = "Report"+fmtDt+".txt";
        FileWriter writer = new FileWriter(Filename);
        writer.append(Report());
        writer.close();
        return true;
    }
    public static String Report(){
        return "";
    }
    
}
