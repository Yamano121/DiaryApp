package borzilov.devdemogit.dict.User.extex;

public class LoginAlreadyTakenException extends RuntimeException{
    public LoginAlreadyTakenException(String login) {
        super("Login already taken: " + login);
    }
}
