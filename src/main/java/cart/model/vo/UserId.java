package cart.model.vo;

import java.util.Objects;

public class UserId {
    private String value;

    private UserId(String value) {
        this.value = value;
    }

    public static UserId of(String userId) {
        return new UserId(userId);
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
        UserId userId = (UserId) o;
        return value.equals(userId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}