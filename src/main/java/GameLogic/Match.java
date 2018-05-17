package GameLogic;

import People.Player;
import People.User;
import Utils.Observer;

import java.util.ArrayList;
import java.util.List;

/**It handles a match*/
public class Match {

    private Board board;
    private Mode modality;
    private long timer, delta;
    private Player player1, player2;
    private PlayingRoutine routine;
    private int winner;
    private List<Observer> observers = new ArrayList<>();
    private int state;

    public int getState() {
        // to be updated with the game state
        return state;
    }

    public void setState(int state) {
        this.state = state;
        notifyAllObservers();
    }

    public void attach(Observer observer){
        observers.add(observer);
    }

    public void notifyAllObservers(){
        for (Observer observer : observers) {
            observer.update();
        }
    }

    /**
     * Create a new game specifying the board dimensions, the modality and the two players
     */
    // TODO : discriminate game mode with multiple constructor
    public Match(int l, int h, Mode mode, User user1, User user2) {
        board = new Board(l, h);
        modality = mode;
        timer = 0;
        this.player1 = new Player(user1, 1);
        this.player2 = new Player(user2, 2);
        switch (mode){
            case MultiPlayer: routine = new MultiPlayerRoutine(this); break;
            case SinglePlayerLevel1: routine = new AIRoutine(this, 4,-3,2,-1); break;
            case SinglePlayerLevel2: routine = new AIRoutine(this, 5,-4,3,-1); break;
        }
    }

    /**
     * Begins a new game
     */
    public void startGame() {
        delta = System.currentTimeMillis();
        routine.execute();
        endGame(winner, true);
    }

    /**
     * Pauses the game
     */
    public void pauseGame() {
        timer += System.currentTimeMillis() - delta;
    }

    /**
     * Restarts the game after pausing
     */
    public void resumeGame() {
        delta = System.currentTimeMillis();
    }

    /**
     * End of the game: assigning scores to the players
     */
    public Board endGame(int w, boolean termine) {
        int gamePlayed;
        if (termine)
            gamePlayed=7+board.getMoveNo()/2;
        else
            gamePlayed=0;
        switch (w) {
            case 1:
                player1.setScore(gamePlayed+board.getMoveNo()%2+50);
                player2.setScore(gamePlayed);
                break;
            case 2:
                player1.setScore(gamePlayed);
                player2.setScore(gamePlayed+board.getMoveNo()%2+50);
                break;
            default:
                player1.setScore(gamePlayed);
                player2.setScore(gamePlayed);
        }
        return board;
    }

    /**
     * Interrupts the game
     */
    public void quitGame() {
    }

    /**
     * Returns the chronometer of the game
     */
    public long getTime() {
        return System.currentTimeMillis() - delta + timer;
    }

    /**
     * Returns the board of the game
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Makes a move on the board in the specified column
     */
    public void move(int col) {
        board.move(col);
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

}