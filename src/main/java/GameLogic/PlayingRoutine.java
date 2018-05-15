package GameLogic;
/**Interface for the routines of the matches*/
public abstract class PlayingRoutine {
    public Match game;

    public PlayingRoutine(Match game) {
        this.game = game;
    }

    public abstract void execute();

    public void vittoria(String message) {
        if (message.equals("1 win!"))
            game.setWinner(1);
        else if (message.equals("2 win!"))
            game.setWinner(2);
        else
            game.setWinner(0);
    }
}