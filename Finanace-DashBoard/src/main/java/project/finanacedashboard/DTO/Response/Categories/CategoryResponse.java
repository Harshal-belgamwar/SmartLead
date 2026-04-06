package project.finanacedashboard.DTO.Response.Categories;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import project.finanacedashboard.Enum.Type;

@Data
public class CategoryResponse {
    private int id;
    private String name;

    private String type;
}
