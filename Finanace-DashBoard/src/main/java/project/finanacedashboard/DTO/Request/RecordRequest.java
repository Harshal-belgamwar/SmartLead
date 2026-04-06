package project.finanacedashboard.DTO.Request;

import lombok.Data;
import project.finanacedashboard.Enum.Type;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class RecordRequest {
    private Long id;
    private String username;        // reference only
    private String categoryName;    // reference only

    private BigDecimal amount;
    private Type type;

    private String description;
}
