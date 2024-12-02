package room;

public class Standard extends Room {
    int Windows;

    public void Standard(String RmNo, int floorNo,String Ocu,String Desc){
        super(RmNo);
    }

    @Override
    public double Price() {
        return 100+(Windows*0.1);
    }

    @Override
    public void display() {
    }
    
}
