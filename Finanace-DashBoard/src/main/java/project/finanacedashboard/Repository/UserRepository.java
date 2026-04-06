package project.finanacedashboard.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.finanacedashboard.Entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query(value = "update User set role_id = null where role_id = :id",nativeQuery = true)
    void UpdateRole(Long id);

}
