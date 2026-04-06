package project.finanacedashboard.ExceptionHandler;

public class DataAlreadyExist extends RuntimeException{
    public DataAlreadyExist(String message){
        super(message);
    }
}
