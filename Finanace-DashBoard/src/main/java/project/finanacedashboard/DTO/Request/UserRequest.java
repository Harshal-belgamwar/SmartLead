package project.finanacedashboard.DTO.Request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserRequest {

    private String username;
    private String email;
    private String password;
    private String role; // VIEWER / ANALYST / ADMIN
}
