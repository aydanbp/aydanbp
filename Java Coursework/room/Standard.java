package room;

public class Standard extends Room {
    private final int Windows;
    private String[] Arr;

    public Standard(String RmNo, int floor, int Ocu ,int Windows) {
        super(RmNo, floor, Ocu);
        this.Windows=Windows;
        Arr[0] = RmNo;
        Arr[1] = Integer.toString(floor);
        Arr[2] = Integer.toString(Ocu);
    }

    @Override
    public void display() {
        System.out.println("Room Number\t"+ Arr[0] +"\t On Floor "+Arr[1]+
        "For Ocu People\n"+"Number of windows: "+Windows);
    }

    @Override
    public double price() {
        return 100*(Windows*0.1);
    }

    @Override
    public void Desc() {
       System.out.println("Damp, Moldy walls and atleast 1 window the size of a hampster. We should be paying you to stay here");
    }



   
    
}
