package room;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DeluxeRoom extends Room{
    private double Balcony;// in square meters
    private String [] View = {"Sea", "Landmark", "Mountain"}; // 1 of 3
    private int i;
    DecimalFormat df = new DecimalFormat("#.##"); // Only two decimal places, used on balcony. Could be used on price?
    

    public DeluxeRoom(String RmNo, int floor, int Ocu ,double Balcony, int i) {
        super(RmNo, floor, Ocu);
        df.setRoundingMode(RoundingMode.CEILING);
        this.Balcony=Double.parseDouble(df.format(Balcony));
        this.i=i; // i will be used to indicate which view is avalible

    }
    @Override
    public double price() {
        return (Balcony * 10) +((i+1) *50) +300;
    }
    @Override
    public void Desc() {
        System.out.println("Nice, warm and some third thing. With your choice of view, you certainly have a night to remember!"+
        "Remember, to choose your view its 0 for Sea, 1 for landmark and 2 for mountain");
    }
    @Override
    public void display() {
        System.out.println("Room Number\t"+ getRoomNo() +"\t Floor No: "+getfloor()+
        "\nFor "+getOcup()+" Person(s)\n"+"The balcony is\t"+Balcony+"^2 m\n"+
        "View Type:\t"+View[i]+
        "\n\tTotal Price:\t£"+price());
    }
    @Override
    public String info() {
        return "Room Number\t"+ getRoomNo() +"\t Floor No: "+getfloor()+
        "\nFor "+getOcup()+" Person(s)\n"+"The balcony is\t"+Balcony+"^2 m\n"+
        "View Type\t"+View[i]+
        "\nTotal Price:\t£"+price();
    }
    @Override
    public String type() {
        return "deluxe";
    }

}
