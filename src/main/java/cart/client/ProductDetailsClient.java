package cart.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import cart.exception.ExternalClientException;
import cart.model.Product;
import cart.model.response.ProductDetails;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;

public class ProductDetailsClient {
    private static String ENDPOINT = "https://equalexperts.github.io/backend-take-home-test-data/{0}.json";
    private ObjectMapper objectMapper;
    private HttpClientFactory httpClientFactory;

    public ProductDetailsClient(ObjectMapper objectMapper, HttpClientFactory httpClientFactory) {
        this.objectMapper = objectMapper;
        this.httpClientFactory = httpClientFactory;
    }

    public ProductDetails getProductDetails(Product product) {
        HttpRequest request = null;
        ProductDetails productDetails = null;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI(MessageFormat.format(ENDPOINT, product.getName())))
                    .GET()
                    .build();

            HttpResponse<InputStream> response = this.httpClientFactory.get()
                    .send(request, HttpResponse.BodyHandlers.ofInputStream());
            productDetails = this.objectMapper.readValue(response.body(), ProductDetails.class);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new ExternalClientException(e);
        }
        return productDetails;
    }

}
