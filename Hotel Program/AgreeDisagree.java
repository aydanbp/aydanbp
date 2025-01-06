
public class AgreeDisagree {

    public static Boolean AgreeDisagree(String Inp)  {
        if (Inp.toLowerCase().contains("yes")||
        Inp.toLowerCase().contains("true")){
            return true;
        }
        else if (Inp.toLowerCase().contains("no")||
        Inp.toLowerCase().matches("false")){
            return false;
            
        }
        else{
            System.out.println("No answer given, 'no' is default answer");
            return false;
            
        }
        
    }
    
}
