package GameLogic;

import java.util.ArrayList;

public class Board{
    private int moveNo, length, height;
    private ArrayList<Integer> lastC, lastR;
    private Cell[][] board;

    public Board(int l, int h) {
        moveNo = 0; //progressivo mossa, contatore
        length = l; //numero di colonne
        height = h; //numero di righe
        lastC =new ArrayList<>(); //colonna dell'ultima mossa
        lastR =new ArrayList<>(); //riga dell'ultima mossa
        board = new Cell[height][length];
        for (int i = 0; i < h; i++)
            for (int j = 0; j < l; j++)
                board[i][j] = new Cell();
    }

    public int getCellOccupant(int r, int c) {
        try {
            return board[r][c].getOccupant();
        } catch (Exception e) {
            return -1;
        }
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
        return lastC.get(lastC.size()-1);
    }

    public int getLastR() {
        return lastR.get(lastR.size()-1);
    }

    public void move(int column) {
        if (column < 0 || column > length - 1)
            throw new IllegalArgumentException(column + " is not an existing column");
        if (!(board[height - 1][column].isEmpty()))
            throw new IllegalArgumentException("Full Column");
        int result = 0;
        for (int i = 0; i < height; i++) {
            if (board[i][column].isEmpty()) {
                moveNo++;
                board[i][column].drop(moveNo);
                lastC.add(column);
                lastR.add(i);
                result = scan(column);
                break;
            }
        }
        if (result != 0)
            throw new RuntimeException(result + " win!");
        if (moveNo == length * height)
            throw new RuntimeException("tie!");
    }
    public void undo(){
        moveNo--;
        board[lastR.get(lastR.size()-1)][lastC.get(lastC.size()-1)].undrop();
        lastR.remove(lastR.size()-1);
        lastC.remove(lastC.size()-1);
    }

    public int scanHorizontal(int c, int line) {
        int r=getLastR();
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

    public int scanVertical(int c, int line) {
        int r=getLastR();
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

    public int scanForDiag(int c, int line) {
        int r=getLastR();
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

    public int scanBacDiag(int c, int line) {
        int r=getLastR();
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


    public int scan(int c) {
        if (moveNo < 7)
            return 0;
        int scan;
        scan = scanHorizontal(c, 4);
        if (scan != 0)
            return scan;
        scan = scanVertical(c, 4);
        if (scan != 0)
            return scan;
        scan = scanForDiag(c, 4);
        if (scan != 0)
            return scan;
        scan = scanBacDiag(c, 4);
        if (scan != 0)
            return scan;
        return 0;
    }
}