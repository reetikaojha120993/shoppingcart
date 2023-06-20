package cart.client;

import java.net.http.HttpClient;

public class HttpClientFactoryImpl implements HttpClientFactory{
    @Override
    public HttpClient get() {
        return HttpClient.newHttpClient();
    }
}
