package cart.exception;

public class InvalidCartIdException extends RuntimeException {
    private static String ERROR_MESSAGE = "Provided cartId is invalid ";

    public InvalidCartIdException() {
        super(ERROR_MESSAGE);
    }

}
