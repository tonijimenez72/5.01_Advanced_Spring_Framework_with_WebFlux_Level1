package cat.itacademy.s05.t01.n01.S05T01N01.enums;

public enum CardSuit {
    HEARTS,
    DIAMONDS,
    CLUBS,
    SPADES;

    @Override
    public String toString() {
        return name().charAt(0) + name().substring(1).toLowerCase();
    }
}