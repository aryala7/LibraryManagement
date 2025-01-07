package la.arya.librarymanagement.excpetion;

public class CategoryNotFoundException extends  RuntimeException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
