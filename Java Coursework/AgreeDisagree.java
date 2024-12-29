public class AgreeDisagree {
    public static boolean AgreeDisagree(String Inp) {
        if (Inp.toLowerCase().contains("yes")){
            return true;
        }
        else{
            return false;
        }
        
    }
    public static void Q() {
        System.out.println("Is this ok?\t>>>");
    }
    
}
