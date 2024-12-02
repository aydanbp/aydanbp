package room;
public class DeluxeRoom extends Room{
    double Balcony;
    String View;

    @Override
    public double Price() {
        double p = 300+(Balcony*0.2);
        return p;
    }
    @Override
    public void display() {
        throw new UnsupportedOperationException("Unimplemented method 'display'");
    }

    
    
}
