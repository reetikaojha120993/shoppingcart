package cart.persistence;

import cart.model.vo.CartId;
import cart.model.vo.UserId;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserCartStore {
    private Map<UserId, CartId> userCartStore;

    public UserCartStore() {
        this.userCartStore = new HashMap<>();
    }

    public CartId get(UserId userId) {
        if (userCartStore.containsKey(userId)) {
            return userCartStore.get(userId);
        } else {
            return create(userId);
        }
    }

    private CartId create(UserId userId) {
        CartId cartId = CartId.of(UUID.randomUUID().toString());
        userCartStore.put(userId, cartId);
        return cartId;
    }
}
