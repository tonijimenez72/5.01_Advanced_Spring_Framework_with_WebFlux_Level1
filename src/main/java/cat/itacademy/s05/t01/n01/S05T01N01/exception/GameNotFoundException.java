package cat.itacademy.s05.t01.n01.S05T01N01.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String message) {
        super(message);
    }
}