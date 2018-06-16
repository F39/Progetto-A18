package GameLogic;

import java.util.ArrayList;

/**
 * Class representing a playground. It keeps memory of the cells it is made of.
 */
public class Board {
    private int moveNo;
    private final int height = 6, length = 7;
    private ArrayList<Integer> lastC, lastR;
    private Cell[][] board;

    /**
     * Create a new empty Board
     */
    public Board() {
        moveNo = 0; // progressivo mossa, contatore
        lastC = new ArrayList<>(); // colonna dell'ultima mossa
        lastR = new ArrayList<>(); // riga dell'ultima mossa
        board = new Cell[height][length];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < length; j++) {
                board[i][j] = new Cell(i, j);
            }
        }
    }

    /**
     * Returns the occupant's number of the specified cell
     */
    public int getCellOccupant(int r, int c) {
        return board[r][c].getOccupant();
    }

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    public int getMoveNo() {
        return moveNo;
    }

    public int getLastC() {
        if(lastC.size()>0)
            return lastC.get(lastC.size() - 1);
        return -1;
    }

    public int getLastR() {
        if(lastR.size()>0)
            return lastR.get(lastR.size() - 1);
        return -1;
    }

    /**
     * Make a move in the specified column
     * Throws IllegalArgumentException in case the column is full or doesn't exist
     * Throws RuntimeException in case of end of the match
     */
    public int move(int column, int turn) {
        if (column < 0 || column > length - 1)
            throw new IllegalArgumentException(column + " is not an existing column");
        if (!(board[height - 1][column].isEmpty()))
            throw new IllegalArgumentException("Full Column");
        int result = 0;
        for (int i = 0; i < height; i++) {
            if (board[i][column].isEmpty()) {
                moveNo++;
                board[i][column].fill(turn);
                lastC.add(column);
                lastR.add(i);
                result = scan();
                break;
            }
        }
        if (moveNo == length * height) {
            result = -1;
        }
        return result;
    }

    /**
     * Opposite of move, retires the last move done
     */
    public void undo() {
        moveNo--;
        board[lastR.get(lastR.size() - 1)][lastC.get(lastC.size() - 1)].empty();
        lastR.remove(lastR.size() - 1);
        lastC.remove(lastC.size() - 1);
    }

    /**
     * Starting from the last move on the specified column, scan the board horizontally searching <line> stones inlined of the same player of the specified cell. Returns the player number in case of success, otherwise 0
     */
    public int scanHorizontal(int line) {
        int r = getLastR();
        int c = getLastC();
        int counter = 0, j;
        int player = board[r][c].getOccupant();
        for (j = 0; j < length; j++) { //row check
            if (board[r][j].getOccupant() == player)
                counter++;
            else
                counter = 0;
            if (counter == line)
                return player;
        }
        return 0;
    }

    /**
     * Starting from the last move on the specified column, scan the board vertically searching <line> stones inlined of the same player of the specified cell. Returns the player number in case of success, otherwise 0
     */
    public int scanVertical(int line) {
        int r = getLastR();
        int c = getLastC();
        int counter = 0, i;
        int player = board[r][c].getOccupant();
        for (i = 0; i <= r; i++) { //column check
            if (board[i][c].getOccupant() == player)
                counter++;
            else
                counter = 0;
            if (counter == line)
                return player;
        }
        return 0;
    }

    /**
     * Starting from the last move on the specified column, scan the board diagonally (/) searching <line> stones inlined of the same player of the specified cell. Returns the player number in case of success, otherwise 0
     */
    public int scanBackDiag(int line) {
        int r = getLastR();
        int c = getLastC();
        int counter = 0, i, j;
        int player = board[r][c].getOccupant();
        if (r - c <= 0) {
            i = 0;
            j = c - r;
        } else {
            i = r - c;
            j = 0;
        }
        for (; i < height && j < length; i++, j++) {   //diagonal forward check
            if (board[i][j].getOccupant() == player)
                counter++;
            else
                counter = 0;
            if (counter == line)
                return player;
        }
        return 0;
    }

    /**
     * Starting from the last move on the specified column, scan the board diagonally (\) searching <line> stones inlined of the same player of the specified cell. Returns the player number in case of success, otherwise 0
     */
    public int scanMainDiag(int line) {
        int r = getLastR();
        int c = getLastC();
        int counter = 0, i, j;
        int player = board[r][c].getOccupant();
        if (r + c <= length - 1) {
            i = 0;
            j = r + c;
        } else {
            i = r + c - length + 1;
            j = length - 1;
        }
        for (; i < height && j >= 0; i++, j--) {   //diagonal backward check
            if (board[i][j].getOccupant() == player)
                counter++;
            else
                counter = 0;
            if (counter == line)
                return player;
        }
        return 0;
    }

    /**
     * Starting from the last move on the specified column, scan the board in all directions checking whether a player has won the match. Returns the player number in case of success, otherwise 0
     */
    public int scan() {
        if (moveNo < 7)
            return 0;
        int scan;
        scan = scanHorizontal(4);
        if (scan != 0)
            return scan;
        scan = scanVertical(4);
        if (scan != 0)
            return scan;
        scan = scanMainDiag(4);
        if (scan != 0)
            return scan;
        scan = scanBackDiag(4);
        if (scan != 0)
            return scan;
        return 0;
    }
}