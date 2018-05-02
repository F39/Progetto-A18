package src.GameLogic;
/**
 * Class representing a single cell of the playground. It keeps memory of the occupant and the turn it has been filled.
 */
public class Cell {
    private int turn, occupant;
/**Create a new empty cell*/
    public Cell() {
        turn = 0;
        occupant = 0;
    }

    public int getOccupant() {
        return occupant; // 1 or 2 if occupied by a player's stone, 0 if empty
    }

    public boolean isEmpty() {
        return occupant == 0;
    }

    public void drop(int moveNo) {
        turn = moveNo / 2 + moveNo % 2; // the turn° stone of this player half of moveNo
        occupant = moveNo % 2 == 0 ? 2 : 1;
    }

    public void undrop() {
        turn = 0;
        occupant = 0;
    }
}