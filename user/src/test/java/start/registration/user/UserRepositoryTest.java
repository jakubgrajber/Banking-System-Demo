package start.registration.user;

import com.smart.tech.start.UserApplication;
import com.smart.tech.start.registration.user.User;
import com.smart.tech.start.registration.user.UserRepository;
import com.smart.tech.start.registration.user.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = UserApplication.class)
@Transactional
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private final static String FIRSTNAME = "James";
    private final static String LASTNAME = "Hetfield";
    private final static String PASSWORD = "password";
    private final static String EMAIL = "j.h@example.com";

    @Test
    @DisplayName("Check if user can be found by his email")
    void shouldBeAbleToFindUserByHisEmail() {
        // given
        User user = new User(FIRSTNAME, LASTNAME, PASSWORD, EMAIL, UserRole.USER);
        userRepository.save(user);

        // when
        Optional<User> optionalUser = userRepository.findByEmail(EMAIL);

        // then
        assertTrue(optionalUser.isPresent());
    }

    @Test
    @DisplayName("Check if user can be found by his email")
    void shouldReturnNullWhenNoOneUserHasSuchAnEmail() {
        // given

        // when
        Optional<User> optionalUser = userRepository.findByEmail(EMAIL);

        // then
        assertFalse(optionalUser.isPresent());
    }

    @Test
    @DisplayName("Check if user can be enabled by his email")
    void shouldBeAbleToEnableUserByHisEmail() {
        // given
        User user = new User(FIRSTNAME, LASTNAME, PASSWORD, EMAIL, UserRole.USER);
        userRepository.save(user);

        // when
        userRepository.enableUser(EMAIL);

        // then
        assertTrue(userRepository.findByEmail(EMAIL).orElseThrow().getEnabled());
    }
}