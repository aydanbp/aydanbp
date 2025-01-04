package User;

import java.time.LocalDate;

public class Customer {

    private String Name, pNum, eMl;
    private LocalDate DoB;

    public Customer(String Name, LocalDate DoB, String pNum, String eMl) {
        this.Name = Name;
        this.DoB = DoB;
        this.pNum = pNum;
        this.eMl = eMl;
    }

    public void display() { // Mainly for testing
        System.out.println("Customer's Name" + Name + "\n"
                + "Customer's Date of birth" + DoB+" Phone Number"+pNum+
                "\nEmail:\t"+eMl);
    }
    public String getName(){
        return Name;
    }

}