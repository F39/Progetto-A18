package src.GameLogic;

import People.Player;

/**Game controller. It handles a match*/
public class Connect4Game {
    private Board board;
    private Mode modality;
    private long timer, delta;
    private Player player1, player2;
    private PlayingRoutine routine;

    /**
     * Create a new game specifying the board dimensions, the modality and the two players
     */
    public Connect4Game(int l, int h, Mode mode, Player player1, Player player2) {
        board = new Board(l, h);
        modality = mode;
        timer = 0L;
        this.player1 = player1;
        this.player2 = player2;
        switch (mode){
            case MultiPlayer: routine=new MultiplayerRoutine(this); break;
            case SinglePlayerLevel1: routine=new AIRoutine(this, 4,-3,2,-1); break;
            case SinglePlayerLevel2: routine=new AIRoutine(this, 5,-4,3,-1); break;
        }
    }

    /**
     * Begins a new game
     */
    public void startGame() {
        delta = System.currentTimeMillis();
        routine.execute();

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
     * Saves the game state
     */
    public Board saveGame() {
        return board;
    }

    /**
     * Interrupts the game without saving
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
}