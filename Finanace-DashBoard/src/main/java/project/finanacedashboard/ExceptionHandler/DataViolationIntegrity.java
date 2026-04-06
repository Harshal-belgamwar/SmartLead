package project.finanacedashboard.ExceptionHandler;

import org.springframework.web.bind.annotation.ExceptionHandler;


public class DataViolationIntegrity extends RuntimeException{
    public DataViolationIntegrity(String message) {
        super(message);
    }
}
