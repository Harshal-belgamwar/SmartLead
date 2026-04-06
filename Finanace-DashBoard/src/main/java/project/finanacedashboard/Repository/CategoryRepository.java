package project.finanacedashboard.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import project.finanacedashboard.Entity.Category;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category,Long> {
    boolean existsByName(String name);

    Optional<Category> findByName(String categoryName);
}
