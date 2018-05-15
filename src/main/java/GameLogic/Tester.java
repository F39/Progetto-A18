package GameLogic;

import People.*;

import java.util.Scanner;

public class Tester {
    private static final int l = 7, h = 6;

    public static void main(String[] args) {
        TerminalIO.setInput(new Scanner(System.in));
        System.out.print("Scegli una modalità (Multiplayer, SinglePlayerLevel1, SinglePlayerLevel2): ");
        int read = TerminalIO.read();
        Mode mode = null;
        for (Mode m : Mode.values()) {
            if (m.ordinal() == read) {
                mode = m;
                break;
            }
        }
        Match game = new Match(l, h, mode != null ? mode : Mode.MultiPlayer, new User("", "", "", ""), new User("", "", "", ""));
        TerminalIO.print(game.getBoard());
        game.startGame();
    }
}