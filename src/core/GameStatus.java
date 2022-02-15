package core;

public enum GameStatus {
    FOUND_MINE("\nResult: Agent dead: found mine\n"),
    WON("\nResult: Agent alive: all solved\n"),
    NO_LOGICAL_MOVES("\nResult: Agent not terminated\n"),
    RUNNING("\nResult: Running");
    private final String message;

    GameStatus(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
