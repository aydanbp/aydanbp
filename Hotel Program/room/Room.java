package room;

public abstract class Room{
    private String RmNo;
    private int floor;
    private int Ocu; //Occupancy
    
    

    public Room(String RmNo, int floor, int Ocu){
        this.floor=floor;
        this.Ocu=Ocu;
        this.RmNo=floor+RmNo;
    }

    public String getRoomNo(){
        return RmNo;
    }
    public int getfloor(){
        return floor;
    }
    public int getOcup(){
        return Ocu;
    }
    
    public abstract double price(); //Price of individual room
    public abstract void Desc();    //General Description of a room type
    public abstract void display(); // Contains details relating to that specific room
    public abstract String info(); //returns string version of display
    public abstract String type(); //returns the type of room
}
