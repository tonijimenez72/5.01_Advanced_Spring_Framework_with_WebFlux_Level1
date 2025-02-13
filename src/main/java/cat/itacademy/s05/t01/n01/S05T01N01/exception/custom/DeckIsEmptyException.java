package cat.itacademy.s05.t01.n01.S05T01N01.exception.custom;

public class DeckIsEmptyException extends RuntimeException {
    public DeckIsEmptyException(String message) {
        super(message);
    }
}