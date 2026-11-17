package ph.dev.dscommerce.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ph.dev.dscommerce.entities.User;
import ph.dev.dscommerce.projections.UserDetailsProjection;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(nativeQuery = true, value = "SELECT u.email AS username, u.password, r.id AS roleId, r.authority " +
            "FROM tb_user u " +
            "INNER JOIN tb_user_role ur ON u.id = ur.user_id " +
            "INNER JOIN tb_role r ON r.id = ur.role_id " +
            "WHERE u.email = :email")
    List<UserDetailsProjection> searchUserAndRolesByUsername(String email);
}
