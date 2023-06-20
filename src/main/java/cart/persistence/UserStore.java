package cart.persistence;

import cart.model.User;
import cart.model.vo.UserId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserStore {
    private List<User> userStore;

    public UserStore() {
        this.userStore = new ArrayList<>();
    }

    public User add(String userName) {
        User user = User.of(UserId.of(UUID.randomUUID().toString()), userName);
        userStore.add(user);
        return user;
    }
    public Boolean has(UserId userId) {
        return userStore.stream().anyMatch(user -> user.getUserId().equals(userId));
    }

    public Boolean remove(User user) {
        return userStore.remove(user);
    }
}
