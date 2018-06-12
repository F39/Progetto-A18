package GameLogic.ArtificialIntelligence;

import GameLogic.Board;
import GameLogic.Match;

public class AIStrategy4StepForecasting implements AIStrategyInt {

    private Match match;
    private Board copiedBoard;
    private int length, height;
    private int[] future;
    private int[] pesi;

    public AIStrategy4StepForecasting(Match match, int a, int b, int c, int d) {
        this.match = match;
        this.copiedBoard = match.getBoard();
        this.length = copiedBoard.getLength();
        this.height = copiedBoard.getHeight();
        this.pesi = new int[]{a, b, c, d};
    }

    @Override
    public int move() {
        int guess;
        if (copiedBoard.getMoveNo() <= 2) {
            if ((guess = (int) (Math.random() * 3 + copiedBoard.getLastC() - 1)) < 0)
                guess++;
            else if ((guess = (int) (Math.random() * 3 + copiedBoard.getLastC() - 1)) > copiedBoard.getLength() - 1)
                guess--;
            return guess; //prime due mosse
        } else {
            future = new int[length];
            for (int i = 0; i < length; i++)
                future[i] = 0;
            guess = forecast();
            if (copiedBoard.getCellOccupant(height - 1, guess) == 0)
                return guess; //uso della previsione
            else {
                return (int) (Math.random() * length); // previsione fallita
            }
        }
    }

    private int forecast() {
        boolean imoved = false, jmoved = false, kmoved = false, lmoved = false;

        for (int i = 0; i < length; i++) {
            copiedBoard = match.getBoard();
            try {
                if (copiedBoard.getCellOccupant(height - 1, i) == 0) {
                    imoved = true;
                    copiedBoard.move(i, 2);
                    future[i] = test(i, pesi[0]);
                } else
                    future[i] = -1000; // è full column
            } catch (RuntimeException e) { // mossa vincente
                if (imoved)
                    copiedBoard.undo();
                return i;
            }
            for (int j = 0; j < length; j++) {
                try {
                    if (copiedBoard.getCellOccupant(height - 1, j) == 0) {
                        jmoved = true;
                        copiedBoard.move(j, 2);
                        future[i] += test(j, pesi[1]);
                    }
                } catch (Exception e) {
                    future[i] += test(j, pesi[1]);
                }
                for (int k = 0; k < length; k++) {
                    try {
                        if (copiedBoard.getCellOccupant(height - 1, k) == 0) {
                            kmoved = true;
                            copiedBoard.move(k, 2);
                            future[i] += deepTest(k, pesi[2]);
                        }
                    } catch (Exception e) {
                        future[i] += deepTest(k, pesi[2]);
                    }
                    for (int l = 0; l < length; l++) {
                        try {
                            if (copiedBoard.getCellOccupant(height - 1, l) == 0) {
                                lmoved = true;
                                copiedBoard.move(l, 2);
                                future[i] += deepTest(l, pesi[3]);
                            }
                        } catch (Exception e) {
                            future[i] += deepTest(l, pesi[3]);
                        }
                        if (lmoved)
                            copiedBoard.undo();
                        lmoved = false;
                    }
                    if (kmoved)
                        copiedBoard.undo();
                    kmoved = false;
                }
                if (jmoved)
                    copiedBoard.undo();
                jmoved = false;
            }
            if (imoved)
                copiedBoard.undo();
            imoved = false;
        }
        int max = -2000;
        for (int i = 0; i < length; i++) {
            if (future[i] > max)
                max = future[i];
        }
        for (int i = ((int) (Math.random() * length)), count = 0; count < length; count++, i = (i + 1) % length) {
            if (future[i] == max && max > -1000) {
                return i;
            }
        }
        return -1;
    }

    private int test(int col, int weight) {
        int victoryP = 0;
        victoryP += (copiedBoard.scanHorizontal(col, 4) != 0 ? 3 * weight : 0);
        victoryP += (copiedBoard.scanVertical(col, 4) != 0 ? 3 * weight : 0);
        victoryP += (copiedBoard.scanMainDiag(col, 4) != 0 ? 3 * weight : 0);
        victoryP += (copiedBoard.scanBackDiag(col, 4) != 0 ? 3 * weight : 0);
        return victoryP;
    }

    private int deepTest(int col, int weight) {
        int victoryP = test(col, weight);
        victoryP += copiedBoard.scanHorizontal(col, 3) != 0 ? weight : 0;
        victoryP += copiedBoard.scanVertical(col, 3) != 0 ? weight : 0;
        victoryP += copiedBoard.scanMainDiag(col, 3) != 0 ? weight : 0;
        victoryP += copiedBoard.scanBackDiag(col, 3) != 0 ? weight : 0;
        return victoryP;
    }

}
