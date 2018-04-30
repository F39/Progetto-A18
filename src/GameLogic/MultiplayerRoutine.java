package src.GameLogic;

public class MultiplayerRoutine implements PlayingRoutine {
    private Connect4Game game;

    public MultiplayerRoutine(Connect4Game game) {
        this.game = game;
    }

    public void execute() {
        while (true) {
            try {
                game.move(TerminalIO.read());
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                break;
            } finally {
                TerminalIO.print(game.getBoard());
            }
        }
    }
}