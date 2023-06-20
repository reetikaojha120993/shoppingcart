package cart.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class ProductDetails {
    @JsonProperty("title")
    private String title;

    @JsonProperty("price")
    private BigDecimal price;

    public ProductDetails(String title, BigDecimal price) {
        this.title = title;
        this.price = price;
    }
    public ProductDetails() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public BigDecimal getPrice() {
        return price;
    }

}