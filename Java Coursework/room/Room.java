package room;

public abstract class Room {
    private String RmNo;
    private int floor;
    private String Ocu;
    private String Desc;

    private void Room(String RmNo, int floor){
        this.RmNo=RmNo;
        this.floor=floor;
    }
    public String Num(){
        return RmNo;
    }

    public abstract double Price();// Used un other classes to calculate price
    public abstract void display();//Display text
}
