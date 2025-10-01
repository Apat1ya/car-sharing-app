package mate.carsharingapp.exception;

public class PermissionDeniedException extends RuntimeException {
    public PermissionDeniedException(String string) {
        super(string);
    }
}
