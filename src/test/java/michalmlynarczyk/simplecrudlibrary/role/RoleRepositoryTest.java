package michalmlynarczyk.simplecrudlibrary.role;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoleRepositoryTest {
    private final RoleRepository underTest;

    @Autowired
    RoleRepositoryTest(RoleRepository underTest) {
        this.underTest = underTest;
    }


    @Test
    void shouldFindRoleByName() {
        //given
        String roleName = "test";
        Role role = new Role(roleName);
        underTest.save(role);

        //when
        Optional<Role> expected = underTest.findRoleByName(roleName);

        //then
        assertThat(expected).isPresent();
        assertThat(role).isEqualTo(expected.get());
    }
}