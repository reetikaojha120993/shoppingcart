package cart.client;

import java.net.http.HttpClient;

public interface HttpClientFactory {
    HttpClient get();
}
