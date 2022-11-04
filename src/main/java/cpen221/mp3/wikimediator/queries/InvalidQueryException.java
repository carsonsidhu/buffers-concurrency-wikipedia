package cpen221.mp3.wikimediator.queries;


//An exception for when a query in WikiMediator cannot be properly parsed i.e. does not satisfy the grammar
public class InvalidQueryException extends Exception{

    public InvalidQueryException(String message){
        super (message);
    }
}
