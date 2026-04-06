package project.finanacedashboard.ExceptionHandler;

public class DataNotFound extends RuntimeException{
    public DataNotFound(String message){
        super(message);
    }
}
