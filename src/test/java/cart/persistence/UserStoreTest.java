package cart.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import cart.model.User;
import cart.model.vo.UserId;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
public class UserStoreTest {
    private UserStore userStore;

    @BeforeEach
    public void init() {
        userStore = new UserStore();
    }

    @Test
    void testAddUser() {
        //given
        String johndoe = "johndoe";
        //when
        User user = userStore.add(johndoe);

        //then
        //Verify a new user is persisted in store if it does not exist
        Assertions.assertAll(
                () -> assertNotNull(user, "User is not null"),
                () -> assertNotNull(user.getUserId(), "Userid is not null"),
                () -> assertEquals(johndoe, user.getName(), "User name matches")
        );
    }

    @Test
    void testAddedUserIsPresent() {
        //given
        String johndoe = "johndoe";
        //when
        User user = userStore.add(johndoe);
        Boolean hasUser = userStore.has(user.getUserId());
        //then
        //Verify if user is existing
        Assertions.assertAll(
                () -> assertTrue(hasUser, "User is persisted")
        );
    }

    @Test
    void testNotAddedUserIsNotPresent() {
        //given
        String johndoe = "johndoe";
        UserId toBeVerified = UserId.of("rojha");
        //when
        userStore.add(johndoe);
        Boolean hasUser = userStore.has(toBeVerified);
        //then
        //Verify if user is existing
        Assertions.assertAll(
                () -> assertFalse(hasUser, "User is not present as it was not persisted")
        );
    }

    @Test
    void testRemoveUser() {
        //given
        String johndoe = "johndoe";
        //when
        User user = userStore.add(johndoe);
        Boolean isUserRemoved = userStore.remove(user);
        //then
        //Verify if user is removed
        Assertions.assertAll(
                () -> assertTrue(isUserRemoved, "User is removed")
        );
    }

    @Test
    void testRemoveUserWhenUserNotPresent() {
        //given
        String johndoe = "johndoe";
        User toBeRemoved = User.of(UserId.of("id"), "rojha");
        //when
        userStore.add(johndoe);
        Boolean isUserRemoved = userStore.remove(toBeRemoved);
        //then
        //Verify user which is not persisted in store cant be removed
        Assertions.assertAll(
                () -> assertFalse(isUserRemoved, "User is not removed as it is not present")
        );
    }

}
