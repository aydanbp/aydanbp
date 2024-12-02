package User;
public class Customer {
    private String name;
    private String DoB;
    private int pNum;
    private Boolean paid;
    private Boolean chkin;

public Boolean Customer(String nm,String sNm, int Mth, int Yer){
    name = nm+sNm;
    DoB = Integer.toString(Mth)+"/"+Integer.toString(Yer);
        return true;
    }

public void display(){
    System.out.println("Customer's Name" + name + "\n"
    +"Customer's Date of birth"+DoB
    );
}
    

}
