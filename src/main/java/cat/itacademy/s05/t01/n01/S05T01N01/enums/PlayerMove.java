package cat.itacademy.s05.t01.n01.S05T01N01.enums;

public enum PlayerMove {
    HIT("Hit"),
    STAND("Stand");

    private final String displayName;

    PlayerMove(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}