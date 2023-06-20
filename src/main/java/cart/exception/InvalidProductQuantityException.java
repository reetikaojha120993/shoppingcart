package cart.exception;

public class InvalidProductQuantityException extends RuntimeException{
    private static String ERROR_MESSAGE = "Provided quantity of product is invalid";

    public InvalidProductQuantityException() {
        super(ERROR_MESSAGE);
    }
}
