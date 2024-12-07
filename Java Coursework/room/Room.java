package room;

public abstract class Room {
    private String RmNo;
    private int floor;
    private int Ocu; //Occupancy
    public String[] Arr;
    
    

    public Room(String RmNo, int floor, int Ocu){
        this.floor=floor;
        this.Ocu=Ocu;
        this.RmNo=RmNo;
    }

    
    public abstract double price(); //Price of individual room
    public abstract void Desc();    //General Description of a room type
    public abstract void display(); // Contains details relating to that specific room


    //public abstract void doot();  //General test variable

}
