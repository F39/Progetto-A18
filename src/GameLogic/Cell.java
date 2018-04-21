package GameLogic;

public class Cell {
    private int turn, occupant;

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
        turn = moveNo / 2 + moveNo % 2; // the turn° stone of this player
        occupant = moveNo % 2 == 0 ? 2 : 1;
    }
}