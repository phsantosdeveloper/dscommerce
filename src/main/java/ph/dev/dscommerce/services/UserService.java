package ph.dev.dscommerce.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ph.dev.dscommerce.entities.Role;
import ph.dev.dscommerce.entities.User;
import ph.dev.dscommerce.projections.UserDetailsProjection;
import ph.dev.dscommerce.repositories.UserRepository;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        List<UserDetailsProjection> result = userRepository.searchUserAndRolesByUsername(username);

        if(result.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        User user = new User();

        user.setEmail(username);
        user.setPassword(result.get(0).getPassword());

        for(UserDetailsProjection obj : result) {
            Role role = new Role(obj.getRoleId(), obj.getAuthority());
            user.addRole(role);
        }

        return user;
    }
}
