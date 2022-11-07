package michalmlynarczyk.simplecrudlibrary.appuser;

import michalmlynarczyk.simplecrudlibrary.role.Role;

public interface AppUserService {
    Role saveRole(Role role);
    AppUser saveUser(AppUser user);
    AppUser getUserByUsername(String username);
    void connectUserWithRole(String username, String roleName) ;
}
