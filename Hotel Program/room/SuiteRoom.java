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
        if (!Kitchenette){
            System.out.println("Room Number\t"+ getRoomNo() +"\t On Floor "+getfloor()+"\n"+
            "For "+getOcup()+" People\t"+"The living area is\t"+LvnArea+"^2 m\n"+"There are "+bthrm+" bathroom(s)"+
            "\nThe total price is\t£"+price());
        }
        else{
            System.out.println("Room Number\t"+ getRoomNo() +"\t On Floor "+getfloor()+"\n"+
            "For "+getOcup()+" People\t"+"The living area is\t"+LvnArea+"^2 m\n"+"There are "+bthrm+" bathroom(s)"+
            "\nAdditionally you have a 1x1m kitchenette"+
            "\nThe total price is\t£"+price());
        }
    }

    @Override
    public String info() {
        if (!Kitchenette){
           return "Room Number\t"+ getRoomNo() +"\t On Floor "+getfloor()+"\n"+
           "For "+getOcup()+" People\t"+"The living area is\t"+LvnArea+"^2 m\n"+"There are "+bthrm+" bathroom(s)"+
            "\nThe total price is\t£"+price();
        }
        else{
            return "Room Number\t"+ getRoomNo() +"\t On Floor "+getfloor()+"\n"+
           "For "+getOcup()+" People\t"+"The living area is\t"+LvnArea+"^2 m\n"+"There are "+bthrm+" bathroom(s)"+
            "\nAdditionally you have a 1x1m kitchenette"+
            "\nThe total price is\t£"+price();
        }
    }

    @Override
    public String type() {
        return "suite";
    }
}
