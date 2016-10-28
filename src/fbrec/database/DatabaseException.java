package fbrec.database;



/**
 * Aggregative-exception for all errors occuring when using the database.
 * For more convinient error handling
 * @author Daniel
 */
public class DatabaseException extends Exception{
    public DatabaseException(Throwable cause) {
        super(cause);
        //addSuppressed(cause); //only since java 7
    }
    
    @Override
    public String getMessage(){
        return "An error occured when accessing the product database.";
    }
}
