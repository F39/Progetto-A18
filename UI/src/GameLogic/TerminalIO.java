package src.GameLogic;

import java.util.Scanner;

public class TerminalIO {
    private static Scanner tastiera;

    public static void print(Board b) {
        int l=b.getLength();
        int h=b.getHeight();
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
                        sb.append("X\t");
                        break;
                    case 2:
                        sb.append("O\t");
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

    public static void setTastiera(Scanner tastiera) {
        TerminalIO.tastiera = tastiera;
    }
}
