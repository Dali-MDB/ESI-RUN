package transport.core;

public class TitreNonValideException extends Exception { //Exception class for when a title is not valid
    public TitreNonValideException(String message) {
        super(message);
    }
}