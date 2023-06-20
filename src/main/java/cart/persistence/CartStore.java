package cart.persistence;

import cart.exception.InvalidCartIdException;
import cart.exception.InvalidOperationException;
import cart.exception.InvalidProductQuantityException;
import cart.exception.ProductNotFoundException;
import cart.model.Product;
import cart.model.CartProduct;
import cart.model.vo.CartId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CartStore {
    public static final int ZERO = 0;
    Map<CartId, List<CartProduct>> cartStore;

    public CartStore() {
        this.cartStore = new HashMap<>();
    }

    public List<CartProduct> add(CartId cartId, Product product, Integer quantity) {
        if (quantity.equals(ZERO)) {
            throw new InvalidProductQuantityException();
        }
        if (cartStore.containsKey(cartId)) {
            List<CartProduct> productList = cartStore.get(cartId);
            Optional<CartProduct> optionalProductInfo = productList.stream().filter(cartProduct -> cartProduct.getProduct().equals(product)).findFirst();

            if (optionalProductInfo.isPresent()) {
                optionalProductInfo.get().incrementCount(quantity);
            } else {
                productList.add(CartProduct.of(product, quantity));
            }

        } else {
            List<CartProduct> cartProductList = new ArrayList<>();
            cartProductList.add(CartProduct.of(product, quantity));
            cartStore.put(cartId, cartProductList);
        }
        return cartStore.get(cartId);
    }

    public List<CartProduct> get(CartId cartId) {
        return cartStore.get(cartId);
    }

    public List<CartProduct> remove(CartId cartId, Product product, Integer quantity) {
        if (quantity.equals(ZERO)) {
            throw new InvalidProductQuantityException();
        }
        if (cartStore.containsKey(cartId)) {
            List<CartProduct> productList = cartStore.get(cartId);
            Optional<CartProduct> optionalProductInfo = productList.stream().filter(cartProduct -> cartProduct.getProduct().equals(product)).findFirst();

            if (optionalProductInfo.isPresent()) {
                if(optionalProductInfo.get().getCount().compareTo(quantity)>0){
                    optionalProductInfo.get().decrementCount(quantity);
                }else if(optionalProductInfo.get().getCount().compareTo(quantity)==0){
                    productList.remove(CartProduct.of(product,quantity));
                }else {
                    throw new InvalidOperationException();
                }
            } else {
                throw new ProductNotFoundException();
            }
        } else {
            throw new InvalidCartIdException();
        }
        return cartStore.get(cartId);
    }
}
