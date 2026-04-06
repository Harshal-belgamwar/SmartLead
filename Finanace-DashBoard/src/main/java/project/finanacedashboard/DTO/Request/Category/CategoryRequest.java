package project.finanacedashboard.DTO.Request.Category;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class CategoryRequest {

        private String name;


        private String type;

}
