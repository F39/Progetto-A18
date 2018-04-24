import ArtificialAgent.*;
import GameLogic.*;
import People.Player;

import java.util.Scanner;

public class Tester {
    private static final int l = 7, h = 6;
    private static Scanner tastiera;

    public static void main(String[] args) {
        Connect4Game game = new Connect4Game(l, h, Mode.Multiplayer, new Player(1), new Player(2));
        System.out.print("Scegli una modalità (0: Multiplayer, 1: ArtificialAgent1, 2: ArtificialAgent2) ");
        tastiera = new Scanner(System.in);
        int mode = Integer.parseInt(tastiera.nextLine());
        print(game.getBoard());
        switch (mode) {
            case 0:
                multiplayerRoutine(game);
                break;
            case 1:
                artificalRoutine(game, new ArtificialVarLev(game,4,-3,2,-1));
                break;
            case 2:
                artificalRoutine(game, new ArtificialVarLev(game,5,-4,3,-1));
                break;
        }
    }

    public static void multiplayerRoutine(Connect4Game game) {
        while (true) {
            try {
                game.move(read());
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                break;
            } finally {
                print(game.getBoard());
            }
        }
    }

    public static void artificalRoutine(Connect4Game game, ArtificialVarLev cpu) {
        while (true) {
            try {
                game.move(read());
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
                continue;
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                print(game.getBoard());
                break;
            }
            try {
                cpu.move();
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                break;
            } finally {
                print(game.getBoard());
            }

        }
    }

    public static void print(Board b) {
        System.out.println("\nFORZA4");
        StringBuilder sb = new StringBuilder();
        sb.append("\t");
        for (int i = 0; i < l; i++)
            sb.append("\t" + i);
        System.out.println(sb.toString() + "\n");
        sb.delete(0, sb.length());
        for (int i = h - 1; i >= 0; i--) {
            sb.append(i + "\t\t");
            for (int j = 0; j < l; j++) {
                switch (b.getCellOccupant(i, j)) {
                    case 1:
                        sb.append("x\t");
                        break;
                    case 2:
                        sb.append("o\t");
                        break;
                    default:
                        sb.append("-\t");
                }
            }
            System.out.println(sb.toString());
            sb.delete(0, sb.length());
        }
    }

    public static int read() {
        while (true) {
            try {
                return Integer.parseInt(tastiera.nextLine());
            } catch (Exception e) {
            }
        }
    }
}