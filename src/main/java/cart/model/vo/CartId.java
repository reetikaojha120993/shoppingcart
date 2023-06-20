package cart.model.vo;

import java.util.Objects;

public class CartId {
    private String value;

    private CartId(String value) {
        this.value = value;
    }

    public static CartId of(String value) {
        return new CartId(value);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        CartId cartId = (CartId) o;
        return value.equals(cartId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}