public class Board {
    private int moveNo, length, height;
    private Cell[][] board;

    public Board(int l, int h) {
        moveNo = 0;         //turno di gioco
        length = l;       //numero di colonne
        height = h;       //numero di righe
        board = new Cell[height][length];
        for (int i = 0; i < h; i++)
            for (int j = 0; j < l; j++)
                board[i][j] = new Cell();
    }

    public Cell getCell(int r, int c) {
        return board[r][c];
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
                result = scan(i, column);
                break;
            }
        }
        if (result != 0)
            throw new RuntimeException(result + "win!");
    }

    public int scan(int r, int c) {
        if (moveNo < 8)
            return 0;
        int possibleWinner = board[r][c].getOccupant();
        int counter = 0;
        int i, j;
        for (j = 0; j < length; j++) { //row check
            if (board[r][j].getOccupant() == possibleWinner)
                counter++;
            else
                counter = 0;
            if (counter == 4)
                return possibleWinner;
        }
        counter = 0;
        for (i = 0; i <= r; i++) { //column check
            if (board[i][c].getOccupant() == possibleWinner)
                counter++;
            else
                counter = 0;
            if (counter == 4)
                return possibleWinner;
        }
        counter = 0;
        if (r - c <= 0) {
            i = 0;
            j = c - r;
        } else {
            i = r - c;
            j = 0;
        }
        for (; i < height && j < length; i++, j++) {   //diagonal forward check
            if (board[i][j].getOccupant() == possibleWinner)
                counter++;
            else
                counter = 0;
            if (counter == 4)
                return possibleWinner;
        }
        counter = 0;
        if (r + c <= length - 1) {
            i = 0;
            j = r + c;
        } else {
            i = r + c - length + 1;
            j = length - 1;
        }
        for (; i < height && j >= 0; i++, j--) {   //diagonal backward check
            if (board[i][j].getOccupant() == possibleWinner)
                counter++;
            else
                counter = 0;
            if (counter == 4)
                return possibleWinner;
        }
        //counter = 0;
        return 0;
    }
}