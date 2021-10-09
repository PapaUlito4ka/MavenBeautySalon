package exceptions;

public class TableRowNotSelectedException extends Exception {
    public TableRowNotSelectedException() {
        super("You didn't select a row");
    }
}
