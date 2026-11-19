package ph.dev.dscommerce.services;

import org.springframework.stereotype.Service;
import ph.dev.dscommerce.entities.User;
import ph.dev.dscommerce.exceptions.ForbiddenException;

@Service
public class AuthService {

    public final UserService userService;

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public void validateSelfOfAdmin(Long userId) {
        User me = userService.authenticated();
        if(!me.hasRole("ROLE_ADMIN") && !me.getId().equals(userId)) {
            throw new ForbiddenException("Acess denied");
        }
    }

}
