package cart.exception;

import java.text.MessageFormat;

public class ExternalClientException extends RuntimeException {
    private static String ERROR_MESSAGE = "Problem while communicating with  upstream client: {0}";

    public ExternalClientException(Exception e) {
        super(MessageFormat.format(ERROR_MESSAGE, e.getMessage()), e);
    }
}
