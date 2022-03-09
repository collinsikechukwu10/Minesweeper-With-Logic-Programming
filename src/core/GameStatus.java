package core;

public enum GameStatus {
    FOUND_MINE("Result: Agent dead: found mine\n"),
    WON("Result: Agent alive: all solved\n"),
    NO_LOGICAL_MOVES("Result: Agent not terminated\n"),
    RUNNING("Result: Running");
    private final String message;

    GameStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
