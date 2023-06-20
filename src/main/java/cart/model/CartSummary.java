package cart.model;

import java.math.BigDecimal;
import java.util.List;

public class CartSummary {
    private List<CartProduct> productList;
    private BigDecimal subtotal;
    private BigDecimal taxPayable;
    private BigDecimal totalPayable;

    private CartSummary(List<CartProduct> productList, BigDecimal subtotal, BigDecimal taxPayable, BigDecimal totalPayable) {
        this.productList = productList;
        this.subtotal = subtotal;
        this.taxPayable = taxPayable;
        this.totalPayable = totalPayable;
    }

    public static CartSummary of(List<CartProduct> productList, BigDecimal subtotal, BigDecimal taxPayable, BigDecimal totalPayable) {
        return new CartSummary(productList, subtotal, taxPayable, totalPayable);
    }

    public List<CartProduct> getProductList() {
        return productList;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public BigDecimal getTaxPayable() {
        return taxPayable;
    }

    public BigDecimal getTotalPayable() {
        return totalPayable;
    }

    @Override
    public String toString() {
        return "CartSummary{" +
                "productList=" + productList +
                ", subtotal=" + subtotal +
                ", taxPayable=" + taxPayable +
                ", totalPayable=" + totalPayable +
                '}';
    }
}
