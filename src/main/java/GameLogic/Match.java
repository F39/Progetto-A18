package GameLogic;

import DatabaseManagement.User;
import Utils.ObserverGame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * It handles a match
 */
public class Match {

    private int gameId;
    private Board board;
    private long timer, delta;
    private User player1, player2;

    private int scoreP1, scoreP2;

    private List<ObserverGame> observerGames = new ArrayList<>();

    private MatchFlowState matchFlowState;
    private int lastMove; // column of the last cell occupied

    public int getGameId() {
        return gameId;
    }

    public MatchFlowState getMatchFlowState() {
        return matchFlowState;
    }

    public void setMatchFlowState(MatchFlowState state) {
        matchFlowState = state;
        notifyAllObservers();
    }

    public int getLastMove() {
        return lastMove;
    }

    public void setLastMove(int column) {
        lastMove = column;
        board.move(lastMove); // board update and self evaluation
        notifyAllObservers();
    }

    public void attach(ObserverGame observerGame) {
        observerGames.add(observerGame);
    }

    private void notifyAllObservers() {
        for (ObserverGame observerGame : observerGames) {
            observerGame.update(this.gameId);
        }
    }

    /**
     * Create a new game specifying the board dimensions, the gameMode and the two players
     */
    // TODO : discriminate game mode with multiple constructor
    public Match(Mode mode, User user1, User user2) {
        // TODO : how to generate an appropriate game id ?
        this.gameId = 1;
        this.board = new Board();
        if (mode == Mode.MultiPlayer) {
            this.player1 = user1;
            this.player2 = user2;
            this.scoreP1 = 0;
            this.scoreP2 = 0;
        } else {
            this.player1 = user1;
            this.scoreP1 = 0;
            // TODO : here setup the AI player
        }
        this.timer = 0;
    }

    /**
     * Begins a new game
     */
    public void startGame() {
        setMatchFlowState(MatchFlowState.started);
        delta = System.currentTimeMillis();
    }

    /**
     * Pauses the game
     */
    public void pauseGame() {
        setMatchFlowState(MatchFlowState.paused);
        timer += System.currentTimeMillis() - delta;
    }

    /**
     * Restarts the game after pausing
     */
    public void resumeGame() {
        setMatchFlowState(MatchFlowState.running);
        delta = System.currentTimeMillis();
    }

    /**
     * End of the game: assigning scores to the players
     */
    public Board endGame(int w, boolean termine) {
        setMatchFlowState(MatchFlowState.finished);
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
                scoreP1 = gamePlayed + board.getMoveNo() % 2 + 50;
                scoreP2 = gamePlayed;
                break;
            case 2:
                //player1.setScore(gamePlayed);
                //player2.setScore(gamePlayed+board.getMoveNo()%2+50);
                scoreP1 = gamePlayed;
                scoreP1 = gamePlayed + board.getMoveNo() % 2 + 50;
                break;
            default:
                //player1.setScore(gamePlayed);
                //player2.setScore(gamePlayed);
                scoreP1 = gamePlayed;
                scoreP2 = gamePlayed;
        }
        return board;
    }

    /**
     * Interrupts the game
     */
    public void quitGame() {
        setMatchFlowState(MatchFlowState.quitted);
        timer += System.currentTimeMillis() - delta;
    }

    /**
     * Returns the chronometer of the game
     */
    public long getTime() {
        if (matchFlowState.equals(MatchFlowState.running))
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
        setLastMove(col);
    }

    public List<User> getPlayers() {
        return Arrays.asList(player1, player2);
    }

}