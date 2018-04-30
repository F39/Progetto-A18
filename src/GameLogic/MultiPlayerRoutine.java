package src.GameLogic;
/**Handles the development of the match in MultiPlayer mode*/
public class MultiPlayerRoutine implements PlayingRoutine {
    private Connect4Game game;

    public MultiPlayerRoutine(Connect4Game game) {
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