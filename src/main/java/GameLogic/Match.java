package GameLogic;

import DatabaseManagement.User;
import GameLogic.ArtificialIntelligence.AIRoutine;
import Utils.Observer;

import java.util.ArrayList;
import java.util.List;

/**
 * It handles a match
 */
public class Match {

    private Board board;
    private Mode modality;
    private long timer, delta;
    private User player1, player2;
    private int score1, score2;
    private PlayingRoutine routine;
    private int winner;
    private List<Observer> observers = new ArrayList<>();
    private Flux fluxState;
    private int[] moveState;

    public Flux getFluxState() {
        // to be updated with the game state
        return fluxState;
    }

    public void setFluxState(Flux state) {
        this.fluxState = state;
        notifyAllObservers();
    }

    public int[] getMoveState() {
        return moveState;
    }

    public void setMoveState(int r, int c) {
        moveState[0] = r;
        moveState[1] = c;
    }

    public void attach(Observer observer) {
        observers.add(observer);
    }

    public void notifyAllObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    /**
     * Create a new game specifying the board dimensions, the modality and the two players
     */
    // TODO : discriminate game mode with multiple constructor
    public Match(Mode mode, User user1, User user2) {
        board = new Board();
        modality = mode;
        timer = 0;
        this.player1 = user1;
        this.player2 = user2;
        score1 = 0;
        score2 = 0;
        moveState = new int[2]; // coordinates of the last cell occupied
        switch (mode) {
            case MultiPlayer:
                routine = new MultiPlayerRoutine(this);
                break;
            case SinglePlayerLevel1:
                routine = new AIRoutine(this, 4, -3, 2, -1);
                break;
            case SinglePlayerLevel2:
                routine = new AIRoutine(this, 5, -4, 3, -1);
                break;
        }
    }

    /**
     * Begins a new game
     */
    public void startGame() {
        setFluxState(Flux.running);
        delta = System.currentTimeMillis();
        routine.execute();
        endGame(winner, true);
    }

    /**
     * Pauses the game
     */
    public void pauseGame() {
        setFluxState(Flux.paused);
        timer += System.currentTimeMillis() - delta;
    }

    /**
     * Restarts the game after pausing
     */
    public void resumeGame() {
        setFluxState(Flux.running);
        delta = System.currentTimeMillis();
    }

    /**
     * End of the game: assigning scores to the players
     */
    public Board endGame(int w, boolean termine) {
        setFluxState(Flux.finished);
        timer += System.currentTimeMillis() - delta;
        int gamePlayed;
        if (termine)
            gamePlayed = 7 + board.getMoveNo() / 2;
        else
            gamePlayed = 0;
        switch (w) {
            case 1:
                //player1.setScore(gamePlayed+board.getMoveNo()%2+50);
                //player2.setScore(gamePlayed);
                score1 = gamePlayed + board.getMoveNo() % 2 + 50;
                score2 = gamePlayed;
                break;
            case 2:
                //player1.setScore(gamePlayed);
                //player2.setScore(gamePlayed+board.getMoveNo()%2+50);
                score1 = gamePlayed;
                score1 = gamePlayed + board.getMoveNo() % 2 + 50;
                break;
            default:
                //player1.setScore(gamePlayed);
                //player2.setScore(gamePlayed);
                score1 = gamePlayed;
                score2 = gamePlayed;
        }
        return board;
    }

    /**
     * Interrupts the game
     */
    public void quitGame() {
        setFluxState(Flux.quitted);
        timer += System.currentTimeMillis() - delta;
    }

    /**
     * Returns the chronometer of the game
     */
    public long getTime() {
        if (fluxState.equals(Flux.running))
            return System.currentTimeMillis() - delta + timer;
        return timer;
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
        setMoveState(board.getLastR(), board.getLastC());
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }
}