package cart.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import cart.exception.InvalidCartIdException;
import cart.exception.InvalidOperationException;
import cart.exception.InvalidProductQuantityException;
import cart.exception.ProductNotFoundException;
import cart.model.CartProduct;
import cart.model.Product;
import cart.model.vo.CartId;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class CartStoreTest {
    private CartStore cartStore;

    @BeforeEach
    public void init() {
        cartStore = new CartStore();
    }

    @Test
    void testAddSingleProductToCart() {
        //given
        CartId cartId = CartId.of("cartOfJdoe");
        Product cheerios = Product.CHEERIOS;
        Integer quantity = 2;
        //when
        List<CartProduct> cartProductList = cartStore.add(cartId, cheerios, quantity);

        //then
        //Verify if a given product with provided quantity is persisted in the cart
        Assertions.assertAll(
                () -> assertEquals(1, cartProductList.size(), "Count of products added to cart matches"),
                () -> assertEquals(quantity, cartProductList.get(0).getCount(), "Quantity of added product matches")
        );
    }

    @Test
    void testProductWithZeroCountToCart() {
        //given
        CartId cartId = CartId.of("cartOfJdoe");
        Product cheerios = Product.CHEERIOS;
        Integer quantity = 0;
        //when and then
        //Verify if exception is raised when a product with zero quantity is added to cart
        Assertions.assertThrows(InvalidProductQuantityException.class, () ->
                                        cartStore.add(cartId, cheerios, quantity),
                                "InvalidProductQuantityException was expected");

    }

    @Test
    void testAddSingleProductManyTimesToCart() {
        //given
        CartId cartId = CartId.of("cartOfJdoe");
        Product cheerios = Product.CHEERIOS;
        Integer quantity1 = 2;
        Integer quantity2 = 4;
        Integer quantity3 = 5;

        //when
        cartStore.add(cartId, cheerios, quantity1);
        cartStore.add(cartId, cheerios, quantity2);
        List<CartProduct> cartProductList = cartStore.add(cartId, cheerios, quantity3);

        //then
        //Verify if a given product added multiple times with different quantity is persisted in the cart
        Assertions.assertAll(
                () -> assertEquals(1, cartProductList.size(), "Count of products added to cart matches"),
                () -> assertEquals(11, cartProductList.get(0).getCount(), "Quantity of first product matches")
        );
    }

    @Test
    void testAddMultipleProductManyTimesToCart() {
        //given
        CartId cartId = CartId.of("cartOfJdoe");
        Product cheerios = Product.CHEERIOS;
        Integer cheeriosQ1 = 2;
        Integer cheeriosQ2 = 4;
        Integer cheeriosQ3 = 5;

        Product frosties = Product.FROSTIES;
        Integer frostiesQ1 = 7;
        Integer frostiesQ2 = 9;

        //when
        cartStore.add(cartId, cheerios, cheeriosQ1);
        cartStore.add(cartId, cheerios, cheeriosQ2);
        cartStore.add(cartId, cheerios, cheeriosQ3);
        cartStore.add(cartId, frosties, frostiesQ1);
        List<CartProduct> cartProductList = cartStore.add(cartId, frosties, frostiesQ2);

        //then
        //Verify if a given products added multiple times with different quantity are persisted in the cart
        Assertions.assertAll(
                () -> assertEquals(2, cartProductList.size(), "Count of products added to cart matches"),
                () -> assertEquals(11, cartProductList.stream().filter(cartProduct -> cartProduct.getProduct().equals(Product.CHEERIOS)).findFirst().get().getCount(),
                                   "Quantity of first product matches"),
                () -> assertEquals(16, cartProductList.stream().filter(cartProduct -> cartProduct.getProduct().equals(Product.FROSTIES)).findFirst().get().getCount(),
                                   "Quantity of second product matches")
        );
    }

    @Test
    void testRemoveSingleProductToCart() {
        //given
        CartId cartId = CartId.of("cartOfJdoe");
        //when
        cartStore.add(cartId, Product.CHEERIOS, 1);
        cartStore.add(cartId, Product.FROSTIES, 2);
        cartStore.add(cartId, Product.CORNFLAKES, 2);
        cartStore.add(cartId, Product.SHREDDIES, 3);
        List<CartProduct> cartProductList = cartStore.remove(cartId, Product.CHEERIOS, 1);

        //then
        //Verify if a given product and quantity is removed from the cart
        Assertions.assertAll(
                () -> assertEquals(3, cartProductList.size(), "Count of products added to cart matches"),
                () -> assertFalse(cartProductList.stream().anyMatch(cartProduct -> cartProduct.getProduct().equals(Product.CHEERIOS)),
                                  "Provided product does not exist")
        );
    }

    @Test
    void testReduceQuantityOfProductInCart() {
        //given
        CartId cartId = CartId.of("cartOfJdoe");
        //when
        cartStore.add(cartId, Product.CHEERIOS, 1);
        cartStore.add(cartId, Product.FROSTIES, 2);
        cartStore.add(cartId, Product.CORNFLAKES, 2);
        cartStore.add(cartId, Product.SHREDDIES, 3);
        List<CartProduct> cartProductList = cartStore.remove(cartId, Product.FROSTIES, 1);

        //then
        //Verify when a given product and quantity which is less than total count of product in cart
        // is removed then the total count if product in cart reduces
        Assertions.assertAll(
                () -> assertEquals(4, cartProductList.size(), "Count of products added to cart matches"),
                () -> assertTrue(cartProductList.stream().anyMatch(cartProduct -> cartProduct.getProduct().equals(Product.FROSTIES) && cartProduct.getCount() == 1),
                                 "Quantity of product is reduced")
        );
    }

    @Test
    void testReduceQuantityOfProductInCartByInvalidAmount() {
        //given
        CartId cartId = CartId.of("cartOfJdoe");
        //when
        cartStore.add(cartId, Product.CHEERIOS, 1);
        cartStore.add(cartId, Product.FROSTIES, 2);
        cartStore.add(cartId, Product.CORNFLAKES, 2);
        cartStore.add(cartId, Product.SHREDDIES, 3);
        //then
        //Verify when a given products and quantity which is more than total count of product in cart
        // is removed then an exception is raised
        Assertions.assertThrows(InvalidOperationException.class, () ->
                cartStore.remove(cartId, Product.FROSTIES, 100)
        );
    }

    @Test
    void testInvalidCartIdAmount() {
        //given
        CartId cartId = CartId.of("cartOfJdoe");
        //when
        //no product is added to cart and not cart id is generated
        //then
        //Verify when cart is not existing and a given product and quantity which is
        // removed then an exception is raised
        Assertions.assertThrows(InvalidCartIdException.class, () ->
                cartStore.remove(cartId, Product.FROSTIES, 100)
        );
    }

    @Test
    void testInvalidProductRemoval() {
        //given
        CartId cartId = CartId.of("cartOfJdoe");
        //when
        cartStore.add(cartId, Product.CHEERIOS, 1);
        cartStore.remove(cartId, Product.CHEERIOS, 1);
        //then
        //Verify when cart is empty and a given product and quantity which is
        // removed then an exception is raised
        Assertions.assertThrows(ProductNotFoundException.class, () ->
                cartStore.remove(cartId, Product.CHEERIOS, 100)
        );
    }

    @Test
    void testInvalidProductQuantity() {
        //given
        CartId cartId = CartId.of("cartOfJdoe");
        //when
        cartStore.add(cartId, Product.CHEERIOS, 1);
        //then
        //Verify when a given product with zero quantity is
        // removed then an exception is raised
        Assertions.assertThrows(InvalidProductQuantityException.class, () ->
                cartStore.remove(cartId, Product.CHEERIOS, 0)
        );
    }
}
