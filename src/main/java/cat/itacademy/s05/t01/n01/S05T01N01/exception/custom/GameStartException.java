package cat.itacademy.s05.t01.n01.S05T01N01.exception.custom;

public class GameStartException extends RuntimeException {
    public GameStartException(String message) {
        super(message);
    }
}
