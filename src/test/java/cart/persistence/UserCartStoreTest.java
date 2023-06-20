package cart.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import cart.model.vo.CartId;
import cart.model.vo.UserId;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class UserCartStoreTest {
    private UserCartStore userCartStore;

    @BeforeEach
    public void init() {
        userCartStore = new UserCartStore();
    }

    @Test
    void testCreateCart() {
        //given
        UserId newUser = UserId.of("newUser");
        //when
        CartId cartId = userCartStore.get(newUser);
        //then
        //Verify a cart is created for a user if it does not already exist
        Assertions.assertAll(
                () -> assertNotNull(cartId, "A new CartId is generated for user")
        );
    }

    @Test
    void testReturnOldCart() {
        //given
        UserId newUser = UserId.of("newUser");
        CartId cartId = userCartStore.get(newUser);

        //when
        CartId retrievedCartId = userCartStore.get(newUser);

        //then
        //Verify a cart is returned for a user if it already exists
        Assertions.assertAll(
                () -> assertEquals(cartId, retrievedCartId, "CartId once generated is always returned for a single user")
        );
    }

}
