package cart.model;

import java.util.Objects;

public class CartProduct {
    private Product product;
    private Integer count;

    private CartProduct(Product product, Integer count) {
        this.product = product;
        this.count = count;
    }

    public static CartProduct of(Product product, Integer count) {
        return new CartProduct(product, count);
    }

    public Integer getCount() {
        return count;
    }

    public Integer incrementCount(Integer quantity) {
        return count += quantity;
    }

    public Integer decrementCount(Integer quantity) {
        return count -= quantity;
    }

    public Product getProduct() {
        return product;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CartProduct that = (CartProduct) o;
        return product == that.product && count.equals(that.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, count);
    }

    @Override
    public String toString() {
        return "CartProduct{" +
                "product=" + product +
                ", count=" + count +
                '}';
    }
}
