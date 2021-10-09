package exceptions;

public class BlankFilterTextFieldException extends Exception {
    public BlankFilterTextFieldException () {
        super("You didn't enter text for searching");
    }
}

