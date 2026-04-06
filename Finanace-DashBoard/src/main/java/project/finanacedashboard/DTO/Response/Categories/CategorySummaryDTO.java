package project.finanacedashboard.DTO.Response.Categories;

import lombok.Data;
import project.finanacedashboard.Enum.Type;

import java.math.BigDecimal;

@Data
public class CategorySummaryDTO {
    private String categoryName;

    private BigDecimal totalAmount;

    public CategorySummaryDTO(String key, BigDecimal value) {
        this.categoryName = key;
        this.totalAmount = value;
    }
}
