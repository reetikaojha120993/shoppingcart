package cart.exception;

public class ProductNotFoundException extends RuntimeException{
    private static String ERROR_MESSAGE = "Provided product is not present";

    public ProductNotFoundException() {
        super(ERROR_MESSAGE);
    }
}
