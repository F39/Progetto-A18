package Tests;

import GameLogic.Board;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BoardTest {

    private Board board = new Board();

    @BeforeAll
    void beforeAll(){
        this.board.move(0,1);
    }

    @Test
    @DisplayName("Board length test")
    void getLength() {
        assertEquals(7, this.board.getLength());
    }

    @Test
    @DisplayName("Board height test")
    void getHeight() {
        assertEquals(6, this.board.getHeight());
    }

    @Test
    @DisplayName("Get cell occupant test")
    void getCellOccupant() {
        assertEquals(1, this.board.getCellOccupant(0,0));
    }

    @Test
    @DisplayName("Get move number test")
    void getMoveNo() {
        assertEquals(1, this.board.getMoveNo());
    }

    @Test
    @DisplayName("Move out of range exception test")
    void moveOutOfRangeExceptions() {
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> this.board.move(10, 1));
        assertEquals("10 is not an existing column", exception.getMessage());
    }

    @Test
    @DisplayName("Full column exception test")
    void moveFullColumnExceptions() {
        Board board = new Board();
        board.move(0,1);
        board.move(0,1);
        board.move(0,1);
        board.move(0,1);
        board.move(0,1);
        board.move(0,1);
        Throwable exception = assertThrows(IllegalArgumentException.class, () -> board.move(0, 1));
        assertEquals("Full Column", exception.getMessage());
    }

    @Test
    @DisplayName("Horizontal scan test")
    void scanHorizontal() {
        Board board = new Board();
        board.move(0,1);
        board.move(1,1);
        board.move(2,1);
        board.move(3,1);
        assertEquals(1, board.scanHorizontal(0,4));
        assertEquals(0, board.scanHorizontal(4,4));
        assertEquals(1, board.scanHorizontal(0,3));
        assertEquals(0, board.scanHorizontal(4,3));
    }

    @Test
    @DisplayName("Vertical scan test")
    void scanVertical() {
        Board board = new Board();
        board.move(0,1);
        board.move(0,1);
        board.move(0,1);
        board.move(0,1);
        assertEquals(1, board.scanVertical(0,4));
        assertEquals(0, board.scanVertical(1,4));
        assertEquals(1, board.scanVertical(0,3));
        assertEquals(0, board.scanVertical(1,3));
    }

    @Test
    @DisplayName("Main diagonal scan test")
    void scanMainDiag() {
        Board board = new Board();
        board.move(0,1);
        board.move(0,1);
        board.move(0,1);
        board.move(0,1);
        board.move(1,1);
        board.move(1,1);
        board.move(1,1);
        board.move(2,1);
        board.move(2,1);
        board.move(3,1);
        assertEquals(1, board.scanMainDiag(3,4));
        assertEquals(0, board.scanMainDiag(4,4));
        assertEquals(1, board.scanMainDiag(3,3));
        assertEquals(0, board.scanMainDiag(4,3));
    }

    @Test
    @DisplayName("Back diagonal scan test")
    void scanBackDiag() {
        Board board = new Board();
        board.move(3,1);
        board.move(3,1);
        board.move(3,1);
        board.move(3,1);
        board.move(2,1);
        board.move(2,1);
        board.move(2,1);
        board.move(1,1);
        board.move(1,1);
        board.move(0,1);
        assertEquals(1, board.scanMainDiag(0,4));
        assertEquals(0, board.scanMainDiag(5,4));
        assertEquals(1, board.scanMainDiag(0,3));
        assertEquals(0, board.scanMainDiag(5,3));
    }
}