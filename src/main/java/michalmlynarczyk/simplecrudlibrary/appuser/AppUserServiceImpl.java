package michalmlynarczyk.simplecrudlibrary.appuser;

import lombok.extern.slf4j.Slf4j;
import michalmlynarczyk.simplecrudlibrary.exceptions.AlreadyExistsException;
import michalmlynarczyk.simplecrudlibrary.role.Role;
import michalmlynarczyk.simplecrudlibrary.role.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AppUserServiceImpl implements AppUserService, UserDetailsService {
    private final AppUserRepository appUserRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository, RoleRepository roleRepository) {
        this.appUserRepository = appUserRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving role {} to database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public AppUser saveUser(AppUser user) {
        log.info("Saving user {} to database", user.getUsername());
        if (appUserRepository
                .findByUsername(user.getUsername())
                .isPresent()) {
            log.error("User with username: {} already exists",user.getUsername());
            throw new AlreadyExistsException(String.format("User with username: %s already exists",user.getUsername()));
        }
        return appUserRepository.save(user);
    }

    @Override
    public AppUser getUserByUsername(String username) {
        log.info("Fetching user {} from database", username);
        return appUserRepository
                .findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User with username: {} not found",username);
                    return new EntityNotFoundException(String.format("User with username: %s not found",username));
                });
    }

    @Override
    @Transactional
    public void connectUserWithRole(String username, String roleName) {
        log.info("Assigning role {} to user {}",roleName,username);
        AppUser user = appUserRepository
                .findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User with username: {} not found",username);
                    return new EntityNotFoundException(String.format("User with username: %s not found",username));
                });
        Role role = roleRepository.findRoleByName(roleName).orElseThrow(() ->{
            log.error("Role with name: {} not found",roleName);
            return new EntityNotFoundException(String.format("Role with name: %s not found",roleName));
        });
        user.getRoles().add(role);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = appUserRepository
                .findByUsername(username)
                .orElseThrow(() -> {
                    log.error("User with username: {} not found",username);
                    return new EntityNotFoundException(String.format("User with username: %s not found",username));
                });
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
        return new User(user.getUsername(),user.getPassword(),authorities);
    }
}
