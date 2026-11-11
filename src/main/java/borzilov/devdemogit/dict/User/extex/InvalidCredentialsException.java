package borzilov.devdemogit.dict.User.extex;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Invalid login or password");
    }
}