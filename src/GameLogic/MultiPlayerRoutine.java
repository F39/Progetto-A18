package src.GameLogic;
/**Handles the development of the match in MultiPlayer mode*/
public class MultiPlayerRoutine extends PlayingRoutine {

    public MultiPlayerRoutine(Connect4Game game) {
        super(game);
    }

    public void execute() {
        while (true) {
            try {
                game.move(TerminalIO.read());
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (RuntimeException e) {
                vittoria(e.getMessage());
                System.err.println(e.getMessage());
                break;
            } finally {
                TerminalIO.print(game.getBoard());
            }
        }
    }
}