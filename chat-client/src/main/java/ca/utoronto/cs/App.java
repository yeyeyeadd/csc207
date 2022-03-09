package ca.utoronto.cs;
import ca.utoronto.cs.View.LoginFrame;
import  ca.utoronto.cs.View.registerFrame;
import  ca.utoronto.cs.View.UserFrame;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        new ChatClient("localhost", 5556).run();
    }
}
