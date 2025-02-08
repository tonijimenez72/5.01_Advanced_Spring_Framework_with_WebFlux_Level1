package cat.itacademy.s05.t01.n01.S05T01N01.enums;

public enum GameStatus {
    NEW,
    IN_PROGRESS,
    FINISHED;

    @Override
    public String toString() {
        switch (this) {
            case NEW:
                return "New";
            case IN_PROGRESS:
                return "In Progress";
            case FINISHED:
                return "Finished";
            default:
                throw new IllegalArgumentException();
        }
    }
}
