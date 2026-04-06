package project.finanacedashboard.ExceptionHandler;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import project.finanacedashboard.DTO.Error.ErrorResponseDTO;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataViolationIntegrity.class)
    public ErrorResponseDTO handleDataViolationIntegrity(DataViolationIntegrity exception){
        return new ErrorResponseDTO(exception.getMessage(), HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(DataAlreadyExist.class)
    public ErrorResponseDTO handleUserAlreadyExist(DataAlreadyExist exception){
        return new ErrorResponseDTO(exception.getMessage(), HttpStatus.CONFLICT.value());
    }

    @ExceptionHandler(DataNotFound.class)
    public ErrorResponseDTO handleDataNotFound(DataNotFound exception){
        return new ErrorResponseDTO(exception.getMessage(), HttpStatus.NOT_FOUND.value());
    }


}
