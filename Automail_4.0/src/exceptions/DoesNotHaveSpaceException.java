package exceptions;

public class DoesNotHaveSpaceException extends Throwable{
    public DoesNotHaveSpaceException(){
        super("No Space left for robot to carry");
    }
}
