package abstractNumberRepresentation.rational;

public class ZeroExponentialException extends RuntimeException {

    public ZeroExponentialException() { super("Cannot evaluate the expression 0^0"); }
}
