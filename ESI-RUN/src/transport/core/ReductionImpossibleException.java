package transport.core;

public class ReductionImpossibleException extends Exception { //Exception class for when a reduction is not possible
    public ReductionImpossibleException(String message) {
        super(message);
    }
}