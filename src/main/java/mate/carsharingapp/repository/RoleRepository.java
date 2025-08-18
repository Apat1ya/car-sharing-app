package mate.carsharingapp.repository;

import java.util.Optional;
import mate.carsharingapp.model.user.Role;
import mate.carsharingapp.model.user.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(RoleName role);

}
