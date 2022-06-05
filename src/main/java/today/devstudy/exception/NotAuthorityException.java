package today.devstudy.exception;

public class NotAuthorityException extends RuntimeException {
    public NotAuthorityException(String msg) {
        super(msg);
    }
}
