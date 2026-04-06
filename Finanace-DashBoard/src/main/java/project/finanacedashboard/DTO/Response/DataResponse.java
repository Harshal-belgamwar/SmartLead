package project.finanacedashboard.DTO.Response;

import lombok.Data;
import org.springframework.http.HttpStatus;
import project.finanacedashboard.Entity.User;

public class DataResponse {

    private int status;
    private String message;

    public DataResponse(String message){
        this.status = HttpStatus.OK.value();
        this.message = message;
    }
}
