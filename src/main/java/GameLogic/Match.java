package GameLogic;

import DatabaseManagement.User;
import Utils.ObserverGame;

import java.util.*;

/**
 * It handles a match
 */
public class Match {

    private int gameId;
    private Board board;
    private long timer, delta;
    private Map<Integer, Player> players = new HashMap<>();
    private int turn;

    private int scoreP1, scoreP2;

    private List<ObserverGame> observerGames = new ArrayList<>();

    private MatchFlowState matchFlowState;
    /**
     * Create a new game specifying the board dimensions, the gameMode and the two players
     */
    // TODO : discriminate game mode with multiple constructor
    public Match(Mode mode, Player player1, Player player2, int gameId) {
        // TODO : how to generate an appropriate game id ?
        this.gameId = gameId;
        this.board = new Board();
        if (mode == Mode.MultiPlayer) {
            this.players.put(1, player1);
            this.players.put(2, player2);
            this.scoreP1 = 0;
            this.scoreP2 = 0;
        } else {
            this.players.put(1, player1);
            this.scoreP1 = 0;
            // TODO : here setup the AI player
        }
        this.timer = 0;
    }

    private int lastMove; // column of the last cell occupied

    public int getGameId() {
        return gameId;
    }

    public MatchFlowState getMatchFlowState() {
        return matchFlowState;
    }

    public void setMatchFlowState(MatchFlowState state) {
        matchFlowState = state;
        notifyAllObservers(-1);
    }

    public int getLastMove() {
        return lastMove;
    }

    public void setLastMove(int column) {
        lastMove = column;
        board.move(lastMove); // board update and self evaluation
        notifyAllObservers(lastMove);
    }

    public void attach(ObserverGame observerGame) {
        observerGames.add(observerGame);
    }

    private void notifyAllObservers(int lastMove) {
        for (ObserverGame observerGame : observerGames) {
            observerGame.update(this.gameId, lastMove);
        }
    }

    /**
     * Begins a new game
     */
    public void startGame() {
        setMatchFlowState(MatchFlowState.started);
        turn = 1;
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
        //board.move(col);
        setLastMove(col);
    }

    public Map<Integer, Player> getPlayers() {
        return players;
    }

    public int getTurn() {
        return turn;
    }
}