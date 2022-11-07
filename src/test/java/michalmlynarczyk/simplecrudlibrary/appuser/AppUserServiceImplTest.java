package michalmlynarczyk.simplecrudlibrary.appuser;

import michalmlynarczyk.simplecrudlibrary.role.Role;
import michalmlynarczyk.simplecrudlibrary.role.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private AppUserRepository appUserRepository;
    private AppUserServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new AppUserServiceImpl(appUserRepository, roleRepository);
    }

    @Test
    void saveRole() {
        //given
        String name = "name";
        Role role = new Role(name);

        //when
        underTest.saveRole(role);

        //then
        ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);
        verify(roleRepository).save(captor.capture());
        Role capturedRole = captor.getValue();

        assertThat(role).isEqualTo(capturedRole);
    }

    @Test
    void saveUser() {
        //given
        String username = "name";
        String password = "password";
        AppUser user = new AppUser(username, password);

        //when
        underTest.saveUser(user);

        //then
        ArgumentCaptor<AppUser> captor = ArgumentCaptor.forClass(AppUser.class);
        verify(appUserRepository).save(captor.capture());
        AppUser capturedUser = captor.getValue();

        assertThat(user).isEqualTo(capturedUser);
    }

    @Test
    void shouldGetUserByUsername() {
        //given
        String username = "name";
        String password = "password";
        AppUser user = new AppUser(username, password);
        given(appUserRepository
                .findByUsername(any()))
                .willReturn(Optional.of(user));

        //when
        underTest.getUserByUsername(username);

        //then
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(appUserRepository).findByUsername(captor.capture());
        String expected = captor.getValue();

        assertThat(username).isEqualTo(expected);
    }

    @Test
    void shouldNotGetUserByUsername() {
        //given
        String username = "name";
        given(appUserRepository
                .findByUsername(any()))
                .willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.getUserByUsername(username))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format("User with username: %s not found", username));
    }

    @Test
    void shouldConnectUserWithRole() {
        //given
        String username = "name";
        String password = "password";
        AppUser user = new AppUser(username, password);
        String roleName = "name";
        Role role = new Role(roleName);

        given(appUserRepository
                .findByUsername(any()))
                .willReturn(Optional.of(user));
        given(roleRepository
                .findRoleByName(any()))
                .willReturn(Optional.of(role));

        //when
        underTest.connectUserWithRole(username, roleName);

        //then
        assertThat(user.getRoles().size())
                .isEqualTo(1);

        assertThat(user.getRoles().stream().findFirst().get())
                .isEqualTo(role);
    }

    @Test
    void shouldNotConnectUserWithRoleWhenUserNotFound() {
        //given
        String username = "name";
        String roleName = "name";
        given(appUserRepository
                .findByUsername(any()))
                .willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.connectUserWithRole(username, roleName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format("User with username: %s not found", username));
    }

    @Test
    void shouldNotConnectUserWithRoleWhenRoleNotFound() {
        //given
        String username = "name";
        String password = "password";
        AppUser user = new AppUser(username, password);
        String roleName = "name";

        given(appUserRepository
                .findByUsername(any()))
                .willReturn(Optional.of(user));
        given(roleRepository
                .findRoleByName(any()))
                .willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.connectUserWithRole(username, roleName))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format("Role with name: %s not found", roleName));
    }

    @Test
    void shouldLoadUserByUsername() {
        //given
        String username = "name";
        String password = "password";
        String firstRole = "first";
        String secondRole = "second";
        List<GrantedAuthority> authorities = Arrays.asList(
                new SimpleGrantedAuthority(firstRole),
                new SimpleGrantedAuthority(secondRole));

        AppUser user = new AppUser(username, password);
        UserDetails userDetails = new User(username, password, authorities);
        given(appUserRepository
                .findByUsername(any()))
                .willReturn(Optional.of(user));

        //when
        UserDetails expected = underTest.loadUserByUsername(username);

        //then
        assertThat(userDetails).isEqualTo(expected);
    }

    @Test
    void shouldNotLoadUserByUsernameWhenUserNotFound() {
        //given
        String username = "name";
        given(appUserRepository
                .findByUsername(any()))
                .willReturn(Optional.empty());

        //when
        //then
        assertThatThrownBy(() -> underTest.loadUserByUsername(username))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage(String.format("User with username: %s not found", username));
    }
}