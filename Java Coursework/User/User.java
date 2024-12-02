package User;
// Was meant to be used as a superclass for admin & Customer. Very little overlap. Most likely be unused
public class User {
    private String name;

    public User(String N) { //Used for the Customer
    name = N;
        
    }
    public Boolean User(String N, String pw){ //used for admin
        name = N;
        return true;

    }
    public void avRooms(){ // List all Avalible rooms 

    }
    
}
