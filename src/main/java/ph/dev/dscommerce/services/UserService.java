package ph.dev.dscommerce.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ph.dev.dscommerce.dto.UserDTO;
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

    protected User authenticated() {

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
            String username = jwtPrincipal.getClaim("username");
            return userRepository.findByEmail(username).get();
        }
        catch (Exception e) {
            throw new UsernameNotFoundException("Username not found");
        }

    }

    @Transactional(readOnly = true)
    public UserDTO getMe() {
        User user = authenticated();
        return new UserDTO(user);
    }
}
