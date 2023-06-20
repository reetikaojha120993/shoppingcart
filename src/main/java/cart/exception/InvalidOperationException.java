package cart.exception;

public class InvalidOperationException extends RuntimeException{
    private static String ERROR_MESSAGE = "Operation requested is invalid";

    public InvalidOperationException() {
        super(ERROR_MESSAGE);
    }
}
