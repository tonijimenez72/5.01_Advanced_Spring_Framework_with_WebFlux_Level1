package cat.itacademy.s05.t01.n01.S05T01N01.exception.custom;

public class InvalidMoveException extends RuntimeException {
    public InvalidMoveException(String message) {
        super(message);
    }
}