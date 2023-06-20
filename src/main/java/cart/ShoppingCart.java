package cart;

import cart.client.HttpClientFactory;
import cart.client.HttpClientFactoryImpl;
import cart.client.ProductDetailsClient;
import cart.model.CartSummary;
import cart.model.Product;
import cart.model.vo.UserId;
import cart.persistence.CartStore;
import cart.persistence.UserCartStore;
import cart.service.CartService;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ShoppingCart {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        HttpClientFactory httpClientFactory = new HttpClientFactoryImpl();
        CartStore cartStore = new CartStore();
        UserCartStore userCartStore = new UserCartStore();
        ProductDetailsClient productDetailsClient = new ProductDetailsClient(mapper, httpClientFactory);
        CartService cartService = new CartService(cartStore, userCartStore, productDetailsClient);

        //given a user
        UserId userId = UserId.of("johndoe");

        //example input
        cartService.addProductToCart(userId, Product.CORNFLAKES, 1);
        cartService.addProductToCart(userId, Product.CORNFLAKES, 1);
        cartService.addProductToCart(userId, Product.WEETABIX, 1);
        CartSummary summary = cartService.getCartDetails(userId);
        System.out.println(summary);

    }
}
