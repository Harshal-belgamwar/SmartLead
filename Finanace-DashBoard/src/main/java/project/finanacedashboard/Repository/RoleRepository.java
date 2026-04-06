package project.finanacedashboard.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.finanacedashboard.Entity.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role,Long> {
    Optional<Role> findByRole(String role);

    boolean existsByRole(String role);

    String role(String role);
}
