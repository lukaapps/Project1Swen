package exceptions;
/**
 * Monday 3:15 Group 10
 * **/
public class DoesNotHaveSpaceException extends Throwable{
    public DoesNotHaveSpaceException(){
        super("No Space left for robot to carry");
    }
}
