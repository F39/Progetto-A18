package GameLogic;

import People.Player;

public class Connect4Game {
    Board board;
    Mode modality;
    long timer, delta;
    Player player1, player2;

    public Connect4Game(int l, int h, Mode mode, Player player1, Player player2) {
        board = new Board(l, h);
        modality = mode;
        timer = 0L;
        this.player1 = player1;
        this.player2 = player2;
    }

    public void startGame() {
        delta = System.currentTimeMillis();
    }

    public void pauseGame() {
        timer += System.currentTimeMillis() - delta;
    }

    public void resumeGame() {
        delta = System.currentTimeMillis();
    }

    public Board saveGame() {
        return board;
    }

    public void quitGame() {
    }

    public long getTime() {
        return System.currentTimeMillis() - delta + timer;
    }

    public Board getBoard() {
        return board;
    }

    public void move(int col) {
        board.move(col);
    }
}