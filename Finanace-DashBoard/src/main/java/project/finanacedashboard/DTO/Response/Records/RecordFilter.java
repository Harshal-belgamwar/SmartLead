package project.finanacedashboard.DTO.Response.Records;

import lombok.Data;
import project.finanacedashboard.Enum.Type;

import java.time.LocalDate;

@Data
public class RecordFilter {

        private String username;      // optional (for admin you may skip this)
        private LocalDate startDate;  // filter from date
        private LocalDate endDate;    // filter to date
        private String category;      // category name
        private Type type;            // INCOME / EXPENSE

}
