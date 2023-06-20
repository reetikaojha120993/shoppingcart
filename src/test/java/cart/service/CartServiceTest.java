package cart.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import cart.client.ProductDetailsClient;
import cart.model.CartProduct;
import cart.model.CartSummary;
import cart.model.Product;
import cart.model.response.ProductDetails;
import cart.model.vo.CartId;
import cart.model.vo.UserId;
import cart.persistence.CartStore;
import cart.persistence.UserCartStore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CartServiceTest {
    private UserCartStore userCartStore;
    private CartStore cartStore;
    private ProductDetailsClient productDetailsClient;
    private CartService cartService;

    @BeforeAll
    public void init() {
        userCartStore = mock(UserCartStore.class);
        cartStore = mock(CartStore.class);
        productDetailsClient = mock(ProductDetailsClient.class);
        cartService = new CartService(cartStore, userCartStore, productDetailsClient);
    }

    @Test
    void testAddProductCart() {
        //given
        UserId userId = UserId.of("jdoe");
        CartId cartId = CartId.of("cartOfJdoe");
        Product cheerios = Product.CHEERIOS;
        Integer quantity = 2;
        BigDecimal cheeriosPrice = new BigDecimal("10.1");
        ProductDetails cheeriosDetails = new ProductDetails("cheerios", cheeriosPrice);
        when(userCartStore.get(userId)).thenReturn(CartId.of("cartOfJdoe"));
        when(cartStore.add(cartId, cheerios, quantity)).thenReturn(List.of(CartProduct.of(cheerios, quantity)));
        when(productDetailsClient.getProductDetails(cheerios)).thenReturn(cheeriosDetails);
        //when
        CartSummary summary = cartService.addProductToCart(userId, cheerios, quantity);

        //then
        //Verify a product is added to cart and cart summary is returned
        //Verify if cart summary contains subtotal,taxpayable and totalpayable along with all products and their quantities
        BigDecimal expectedSubtotal = BigDecimal.valueOf(20.20).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal expectedTaxPayable = BigDecimal.valueOf(2.52);
        BigDecimal expectedTotalPayable = BigDecimal.valueOf(22.72);
        Assertions.assertAll(
                () -> assertEquals(1, summary.getProductList().size(), "One product id added to cart"),
                () -> assertEquals(quantity, summary.getProductList().get(0).getCount(), "Quantity of added product matches"),
                () -> assertEquals(expectedSubtotal, summary.getSubtotal(), "Expected subtotal matches"),
                () -> assertEquals(expectedTaxPayable, summary.getTaxPayable(), "Expected TaxPayable matches"),
                () -> assertEquals(expectedTotalPayable, summary.getTotalPayable(), "Total tax payable matches")
        );
    }

    @Test
    void testGetCartSummary() {
        //given
        UserId userId = UserId.of("jdoe");
        CartId cartId = CartId.of("cartOfJdoe");
        Product cheerios = Product.CHEERIOS;
        Integer quantity = 2;
        BigDecimal cheeriosPrice = new BigDecimal("10.1");
        ProductDetails cheeriosDetails = new ProductDetails("cheerios", cheeriosPrice);
        when(userCartStore.get(userId)).thenReturn(CartId.of("cartOfJdoe"));
        when(cartStore.get(cartId)).thenReturn(List.of(CartProduct.of(cheerios, quantity)));
        when(productDetailsClient.getProductDetails(cheerios)).thenReturn(cheeriosDetails);
        //when
        //Verify if cart summary contains subtotal,taxpayable and totalpayable along with all products and their quantities
        CartSummary summary = cartService.getCartDetails(userId);
        BigDecimal expectedSubtotal = BigDecimal.valueOf(20.20).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal expectedTaxPayable = BigDecimal.valueOf(2.52).setScale(2, RoundingMode.HALF_EVEN);
        BigDecimal expectedTotalPayable = BigDecimal.valueOf(22.72).setScale(2, RoundingMode.HALF_EVEN);
        Assertions.assertAll(
                () -> assertEquals(1, summary.getProductList().size(), "One product id added to cart"),
                () -> assertEquals(quantity, summary.getProductList().get(0).getCount(), "Quantity of added product matches"),
                () -> assertEquals(expectedSubtotal, summary.getSubtotal(), "Expected subtotal matches"),
                () -> assertEquals(expectedTaxPayable, summary.getTaxPayable(), "Expected TaxPayable matches"),
                () -> assertEquals(expectedTotalPayable, summary.getTotalPayable(), "Total tax payable matches")
        );
    }

}
