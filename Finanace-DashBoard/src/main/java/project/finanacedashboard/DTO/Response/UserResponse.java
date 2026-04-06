package project.finanacedashboard.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import project.finanacedashboard.Entity.User;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String username;
    private int status;
    private String message;

    public UserResponse(User user , String message){
        this.username = user.getUsername();
        this.status = HttpStatus.OK.value();
        this.message = message;
    }
}
