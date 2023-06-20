package cart.model;

import cart.model.vo.UserId;

public class User {
    private UserId userId;
    private String name;

    private User(UserId userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public static User of(UserId userId, String name) {
        return new User(userId, name);
    }

    public UserId getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }
}
