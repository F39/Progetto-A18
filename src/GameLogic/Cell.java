public class Cell {
    private int turn, occupant;

    public Cell() {
        turn = 0;
        occupant = 0;
    }

    public int getOccupant() {
        return occupant;
    }

    public boolean isEmpty() {
        return occupant == 0;
    }

    public void drop(int moveNo) {
        turn = moveNo / 2 + moveNo % 2;
        occupant = moveNo % 2 == 0 ? 2 : 1;
    }

    public int getTurn() {
        return turn;
    }
}