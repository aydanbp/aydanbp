package room;

public class Standard extends Room {
    private int Windows;

    public Standard(String RmNo, int floor, int Ocu ,int Windows) {
        super(RmNo, floor, Ocu);
        this.Windows=Windows;
        
    }

    @Override
    public void display() {
        System.out.println("Room Number\t"+ getRoomNo() +"\t On Floor "+getfloor()+
        "\nFor "+getOcup()+" People.\t"+"Number of windows: "+Windows+
        "\nTotal Price:\t£"+price());
    }

    @Override
    public double price() {
        return 100+(Windows*10);
    }

    @Override
    public void Desc() {
       System.out.println("Damp, Moldy walls and atleast 1 window the size of a hampster. We should be paying you to stay here");
    }

    @Override
    public String info() {
        return "Room Number\t"+ getRoomNo() +"\t On Floor "+getfloor()+
        "\nFor "+getOcup()+" People.\t"+"Number of windows: "+Windows+
        "\nTotal Price:\t£"+price();
    }

    @Override
    public String type() {
        return "standard";
    }

    

   
    
}
