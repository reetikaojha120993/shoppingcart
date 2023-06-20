package cart.service;

import cart.client.ProductDetailsClient;
import cart.model.CartProduct;
import cart.model.CartSummary;
import cart.model.Product;
import cart.model.vo.CartId;
import cart.model.vo.UserId;
import cart.persistence.CartStore;
import cart.persistence.UserCartStore;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class CartService {
    public static final BigDecimal TAX_ON_PRODUCTS = BigDecimal.valueOf(00.125);
    public static final int SCALE = 2;
    private CartStore cartStore;
    private UserCartStore userCartStore;
    private ProductDetailsClient productDetailsClient;

    public CartService(CartStore cartStore, UserCartStore userCartStore, ProductDetailsClient productDetailsClient) {
        this.cartStore = cartStore;
        this.userCartStore = userCartStore;
        this.productDetailsClient = productDetailsClient;
    }

    public CartSummary addProductToCart(UserId userId, Product product, Integer quantity) {
        CartId cartId = this.userCartStore.get(userId);
        List<CartProduct> productList = this.cartStore.add(cartId, product, quantity);
        return getCartSummary(productList);
    }

    public CartSummary getCartDetails(UserId userId) {
        CartId cartId = this.userCartStore.get(userId);
        List<CartProduct> productList = this.cartStore.get(cartId);
        return getCartSummary(productList);
    }

    private CartSummary getCartSummary(List<CartProduct> productList) {
        BigDecimal subtotal = getSubtotal(productList);
        BigDecimal taxPayable = getTaxPayable(subtotal);
        BigDecimal totalPayable = getTotalPayable(subtotal, taxPayable);
        return CartSummary.of(productList, subtotal, taxPayable, totalPayable);
    }

    private BigDecimal getTotalPayable(BigDecimal subtotal, BigDecimal taxPayable) {
        return subtotal.add(taxPayable).setScale(SCALE, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getTaxPayable(BigDecimal subtotal) {
        return subtotal.multiply(TAX_ON_PRODUCTS).setScale(SCALE, RoundingMode.HALF_EVEN);
    }

    private BigDecimal getSubtotal(List<CartProduct> productList) {
        return productList.stream().map(cartProduct -> productDetailsClient.getProductDetails(cartProduct.getProduct()).getPrice().multiply(new BigDecimal(cartProduct.getCount())))
                .reduce(BigDecimal.ZERO, (p1, p2) -> p1.add(p2)).setScale(SCALE, RoundingMode.HALF_EVEN );
    }
}
