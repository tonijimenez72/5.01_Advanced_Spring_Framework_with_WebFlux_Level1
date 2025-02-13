package cat.itacademy.s05.t01.n01.S05T01N01.exception.custom;

public class PlayerCreationException extends RuntimeException {
    public PlayerCreationException(String message) {
        super(message);
    }
}