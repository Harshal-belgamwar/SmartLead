package project.finanacedashboard.Entity;
import jakarta.persistence.*;
import lombok.Data;
import project.finanacedashboard.Enum.Type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_records")
@Data
public class Records {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "Record_Id")
        private Long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_Id", nullable = false)
        private User user;

        @OneToOne
        @JoinColumn(name = "Category_ID",nullable = false)
        private Category category;


        @Column(precision = 10, scale = 2, nullable = false)
        private BigDecimal amount;

        @Enumerated(EnumType.STRING)
        private Type type;


        private String description;

        private LocalDateTime createdAt;

        private Boolean isDeleted = false;


}
