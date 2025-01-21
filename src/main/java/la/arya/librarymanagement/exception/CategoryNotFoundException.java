package la.arya.librarymanagement.exception;

public class CategoryNotFoundException extends  RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
