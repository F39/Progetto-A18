import java.util.Scanner;

public class Tester {
    private static final int l = 7, h = 6;

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
                sb.append(b.getCell(i, j).getOccupant() + "\t");
            }
            System.out.println(sb.toString());
            sb.delete(0, sb.length());
        }
    }

    public static void main(String[] args) {
        Board board = new Board(l, h);
        int lettura;
        print(board);
        Scanner tastiera = new Scanner(System.in);
        for (; true; ) {
            try {
                lettura = Integer.parseInt(tastiera.nextLine());
            } catch (Exception e) {
                continue;
            }
            try {
                board.move(lettura);
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                break;
            } finally {
                print(board);
            }
        }
    }
}