package project.finanacedashboard.Entity;

import jakarta.persistence.*;
import lombok.Data;
import project.finanacedashboard.Enum.Type;

import java.time.LocalDateTime;

@Entity
@Table(name = "categories")
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false , unique = true)
    private String name; // e.g. Food, Salary

    @Enumerated(EnumType.STRING)
    private Type type; // INCOME / EXPENSE

    private LocalDateTime createdAt;


}