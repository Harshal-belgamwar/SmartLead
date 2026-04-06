package project.finanacedashboard.DTO.Error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorResponseDTO {
    private String message;
    private int status;

    public ErrorResponseDTO(String message, int status){
        this.message = message;
        this.status = status;
    }
}
