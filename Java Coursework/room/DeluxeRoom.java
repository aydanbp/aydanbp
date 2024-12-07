package room;
public class DeluxeRoom extends Room{
    private double Balcony;// in square meters
    private String [] View = {"Sea", "Landmark", "Mountain"}; // 1 of 3
    private int i;

    public DeluxeRoom(String RmNo, int floor, int Ocu ,double Balcony, int i) {
        super(RmNo, floor, Ocu);
        this.Balcony=Balcony;
        this.i=i; // i will be used to indicate which view is avalible
    }
    @Override
    public double price() {
        double price = Balcony*10+i*50+300;
        return price;
    }
    @Override
    public void Desc() {
        System.out.println("Nice, warm and some third thing. With your choice of view, you certainly have a night to remember!");
    }
    @Override
    public void display() {
        System.out.println("Room Number\t"+ Arr[0] +"\t On Floor "+Arr[1]+
        "For Ocu People\n"+"Number of windows: ");
    }

}
