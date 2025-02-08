package cat.itacademy.s05.t01.n01.S05T01N01.enums;

public enum PlayerMove {
    HIT,
    STAND,
    DOUBLE,
    SPLIT;

    @Override
    public String toString() {
        switch (this) {
            case HIT:
                return "Hit";
            case STAND:
                return "Stand";
            case DOUBLE:
                return "Double";
            case SPLIT:
                return "Split";
            default:
                throw new IllegalArgumentException();
        }
    }
}

