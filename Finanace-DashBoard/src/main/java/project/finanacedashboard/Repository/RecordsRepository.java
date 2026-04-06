package project.finanacedashboard.Repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.finanacedashboard.Entity.Records;
import project.finanacedashboard.Entity.User;

import java.util.List;

@Repository
public interface RecordsRepository extends JpaRepository<Records,Long> {

    @Modifying
    @Transactional
    @Query(value = "update Records r set r.isDeleted = true where r.category.id = :id")
    void updateRecord(Long id);
    Iterable<Records> findByUser(User user);

    @Query("""
    SELECT 
        COALESCE(SUM(CASE WHEN r.type = 'INCOME' THEN r.amount ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN r.type = 'EXPENSE' THEN r.amount ELSE 0 END), 0)
    FROM Records r
    WHERE r.user.username = :username AND r.isDeleted = false
""")
    Object[] getFinancialSummary(@Param("username") String username);

    @Query("""
    SELECT 
        COALESCE(SUM(CASE WHEN r.type = 'INCOME' THEN r.amount ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN r.type = 'EXPENSE' THEN r.amount ELSE 0 END), 0)
    FROM Records r
    WHERE r.isDeleted = false
""")
    Object[] getGlobalFinancialSummary();

    List<Records> findByIsDeletedFalse();

    List<Records> findByUserUsernameAndIsDeletedFalse(String username);
}
