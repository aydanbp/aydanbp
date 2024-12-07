package room;

public class SuiteRoom extends Room {
    int LvnArea; //In square meters
    int bthrm; //No upper or lower limit specified
    boolean Kitchenette; // y/n no null (False by Default)

    public SuiteRoom(String RmNo, int floor, int Ocu ,int LvnArea,int bthrm,boolean Kitchenette) {
        super(RmNo, floor, Ocu);
        this.LvnArea=LvnArea;
        this.bthrm = bthrm;
        this.Kitchenette=Kitchenette;
    }

    @Override
    public double price() {
        double p=500+LvnArea*20+bthrm*50;
        if (Kitchenette=true){
            p=p+250;
        }
        return p;
    }

    @Override
    public void Desc() {
        System.out.println("Very nice very cool. Bathroom, kitchen and living room");
    }

    @Override
    public void display() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'display'");
    }
}
