package src.GameLogic;
/**Handles the development of the match in Single Player mode, in both the levels of difficulty*/
public class AIRoutine extends PlayingRoutine {
    private ArtificialVarLev cpu;
    public AIRoutine(Connect4Game game, int a, int b, int c, int d) {
        super(game);
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
                vittoria(e.getMessage());
                System.err.println(e.getMessage());
                TerminalIO.print(game.getBoard());
                break;
            }
            try {
                cpu.move();
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
