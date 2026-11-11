package borzilov.devdemogit.dict.User.extex;

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException() {
        super("Unauthorized: please log in");
    }
}