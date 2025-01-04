public interface AgreeDisagree {

    
    public static boolean AgreeDisagree(String Inp) {
        if (Inp.toLowerCase().contains("yes")||
        Inp.toLowerCase().matches("ok")||
        Inp.toLowerCase().contains("true")){
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
