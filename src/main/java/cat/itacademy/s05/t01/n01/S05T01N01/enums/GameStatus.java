package cat.itacademy.s05.t01.n01.S05T01N01.enums;

public enum GameStatus {
    NEW("New"),
    IN_PROGRESS("In Progress"),
    FINISHED("Finished");

    private final String displayName;

    GameStatus(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}