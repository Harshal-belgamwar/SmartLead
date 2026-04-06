package project.finanacedashboard.Entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false,unique = true)
    private String username;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name= "Hashed_password",nullable = false)
    private String password;

    @Column(nullable = false)
    private String status = "ACTIVE" ;

    private LocalDateTime createdAt;
}