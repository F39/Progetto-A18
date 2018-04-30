package src.GameLogic;
/**Handles the development of the match in Single Player mode, in both the levels of difficulty*/
public class AIRoutine implements PlayingRoutine {
    private ArtificialVarLev cpu;
    private Connect4Game game;
    public AIRoutine(Connect4Game game, int a, int b, int c, int d) {
        this.game=game;
        cpu=new ArtificialVarLev(game, a, b, c, d);
    }

    @Override
    public void execute() {
        while (true) {
            try {
                game.move(TerminalIO.read());
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
                continue;
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                TerminalIO.print(game.getBoard());
                break;
            }
            try {
                cpu.move();
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
