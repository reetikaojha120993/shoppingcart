package cart.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import cart.exception.ExternalClientException;
import cart.model.Product;
import cart.model.response.ProductDetails;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductDetailsClientTest {
    private ProductDetailsClient productDetailsClient;
    private ObjectMapper objectMapper;
    private HttpClientFactory httpClientFactory;

    @BeforeAll
    public void init() {
        objectMapper = new ObjectMapper();
        httpClientFactory = mock(HttpClientFactory.class);
        productDetailsClient = new ProductDetailsClient(objectMapper, httpClientFactory);
    }

    @Test
    void testGetProductDetails() throws IOException, InterruptedException {
        //given
        String initialString = "{\"title\":\"Cheerios\",\"price\":8.43}";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());

        HttpClient mockHttpClient = mock(HttpClient.class);
        HttpResponse mockHttpResponse = mock(HttpResponse.class);
        when(httpClientFactory.get()).thenReturn(mockHttpClient);
        when(mockHttpClient.send(any(), any())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn(targetStream);

        //when
        //Verify if product details are retrieved and transformed
        ProductDetails productDetails = productDetailsClient.getProductDetails(Product.FROSTIES);
        Assertions.assertAll(
                () -> assertNotNull(productDetails, "ProductDetails is present"),
                () -> assertEquals("Cheerios", productDetails.getTitle(), "Title of product matches")
        );
    }

    @Test
    void testInvalidResponseFromUpstream() throws IOException, InterruptedException {
        //given
        String initialString = "{\"titleINVALID\":\"Cheerios\",\"price\":8.43}";
        InputStream targetStream = new ByteArrayInputStream(initialString.getBytes());

        HttpClient mockHttpClient = mock(HttpClient.class);
        HttpResponse mockHttpResponse = mock(HttpResponse.class);
        when(httpClientFactory.get()).thenReturn(mockHttpClient);
        when(mockHttpClient.send(any(), any())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.body()).thenReturn(targetStream);

        //when
        //Verify to raise exception when response from upstream is not as per expected format
        Assertions.assertThrows(ExternalClientException.class, () ->
                                        productDetailsClient.getProductDetails(Product.FROSTIES),
                                "Raise exception for invalid response"
        );
    }

    @Test
    void testConnectionIssueWithUpstream() throws IOException, InterruptedException {
        //given
        HttpClient mockHttpClient = mock(HttpClient.class);
        when(httpClientFactory.get()).thenReturn(mockHttpClient);
        when(mockHttpClient.send(any(), any())).thenThrow(new InterruptedException());

        //when
        //Verify to raise exception when connection from upstream is interrupted
        Assertions.assertThrows(ExternalClientException.class, () ->
                                        productDetailsClient.getProductDetails(Product.FROSTIES),
                                "Raise exception for interrupted connection"
        );
    }

    @Test
    void testIOExceptionFromEndpoint() throws IOException, InterruptedException {
        //given
        HttpClient mockHttpClient = mock(HttpClient.class);
        when(httpClientFactory.get()).thenReturn(mockHttpClient);
        when(mockHttpClient.send(any(), any())).thenThrow(new IOException());

        //when
        //Verify to raise exception when there is an IO exception while communicating with ui
        Assertions.assertThrows(ExternalClientException.class, () ->
                                        productDetailsClient.getProductDetails(Product.FROSTIES),
                                "Raise exception for IO operations"
        );
    }

}
