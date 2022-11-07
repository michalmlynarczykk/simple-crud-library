package michalmlynarczyk.simplecrudlibrary.appuser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

@DataJpaTest
class AppUserRepositoryTest {
    private final AppUserRepository underTest;

    @Autowired
    AppUserRepositoryTest(AppUserRepository underTest) {
        this.underTest = underTest;
    }

    @Test
    void shouldFindUserByUsername() {
        //given
        String username = "user";
        AppUser user = new AppUser(username, "passwd");
        underTest.save(user);

        //when
        Optional<AppUser> expected = underTest.findByUsername(username);

        //then
        assertThat(expected).isPresent();
        assertThat(user).isEqualTo(expected.get());
    }

    @Test
    void shouldNotFindUserByUsername() {
        //given
        AppUser user = new AppUser("user", "passwd");
        underTest.save(user);

        //when
        Optional<AppUser> expected = underTest.findByUsername("notuser");

        //then
        assertThat(expected).isEmpty();
    }
}