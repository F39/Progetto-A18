package GameLogic;

/**
 * Class representing a single cell of the playground. It keeps memory of the occupant from witch it has been filled.
 */
public class Cell {

    private int occupant;
    private int x, y;

    public Cell(int x, int y) {
        occupant = 0;
        this.x = x;
        this.y = y;
    }

    public void empty() {
        this.occupant = 0;
    }

    public int getOccupant() {
        return occupant;
    }

    public boolean isEmpty() {
        return occupant == 0;
    }

    public void fill(int turn) {
        occupant = turn;
    }

}