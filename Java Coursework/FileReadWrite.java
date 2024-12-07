import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class FileReadWrite {

        public void FileWrite() throws IOException{
        //This part retrieves the local date and uses it as the CSV name
        DateTimeFormatter fmtObj = DateTimeFormatter.ofPattern("ddMMyy");
        LocalDate rawDt = LocalDate.now();
        String fmtDt = rawDt.format(fmtObj);
        System.out.println(fmtDt);

        String Filename = fmtDt+".csv";
        FileWriter writer = new FileWriter(Filename);
        writer.append("RoomNo.,Name,Phone Number \n");
        writer.close();
        }

        public void FileRead() throws IOException {
            // This read a file and displays it on the console
            File path = new File(".csv");
            Scanner sc = new Scanner(path);
            sc.useDelimiter(",");
            while(sc.hasNext()){
                System.out.print(sc.next()+"\t");
            }
            sc.close(); 
        }
            //Split method - Splits CSV into arrays then displays information
        public void FileReadS() throws IOException{
            String FlNm = ".csv";
            FileReader reader = new FileReader(FlNm);
            BufferedReader bfrd = new BufferedReader(reader);
            String line;
            while((line=bfrd.readLine())!=null){
                String[] data = line.split(",");
                for(String value:data){
                    System.out.println(value+"\t");
                }
                System.out.println("");
            }
        }

}
