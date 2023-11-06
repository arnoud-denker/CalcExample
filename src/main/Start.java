package main;

public class Start 
    {
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) 
        {
        try {
            MainEngine mainengine = new MainEngine();
            
            Thread.sleep(5000);
            
            mainengine.start();
            } 
        catch (InterruptedException ex) 
            {
            ex.printStackTrace();
            }
        }
    
}
