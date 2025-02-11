package cat.itacademy.s05.t01.n01.S05T01N01.exception;

public class GameAlreadyEndedException extends RuntimeException {
    public GameAlreadyEndedException(String message) {
        super(message);
    }
}