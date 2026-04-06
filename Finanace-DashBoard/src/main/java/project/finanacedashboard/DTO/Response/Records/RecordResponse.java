package project.finanacedashboard.DTO.Response.Records;

import lombok.Data;
import lombok.NoArgsConstructor;
import project.finanacedashboard.Enum.Type;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RecordResponse {
    private Long id;
    private String username;        // reference only
    private String categoryName;    // reference only

    private BigDecimal amount;
    private Type type;

    private String description;
    private LocalDateTime date;
}
