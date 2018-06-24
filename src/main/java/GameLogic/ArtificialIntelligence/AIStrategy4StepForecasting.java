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
            guess = (int) (Math.random() * 3 + copiedBoard.getLastC() - 1);
            if (guess < 0)
                guess++;
            else if (guess > length - 1)
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
        int rincaro = 4;
        copiedBoard = match.getBoard();
        for (int i = 0; i < length; i++) {
            if (copiedBoard.getCellOccupant(height - 1, i) == 0) {
                imoved = true;
                copiedBoard.move(i, 2);
                future[i] = test(rincaro * pesi[0], 4);
                if (future[i] != 0) { // mossa vincente: interrompi
                    if (imoved) {
                        copiedBoard.undo();
                        imoved = false;
                    }
                    return i;
                }
            } else
                future[i] = -1000; // è full column
            for (int j = 0; j < length; j++) {
                if (copiedBoard.getCellOccupant(height - 1, j) == 0) {
                    jmoved = true;
                    copiedBoard.move(j, 1);
                    future[i] += test(rincaro * pesi[1], 4);
                }
                for (int k = 0; k < length; k++) {
                    if (copiedBoard.getCellOccupant(height - 1, k) == 0) {
                        kmoved = true;
                        copiedBoard.move(k, 2);
                        future[i] += test(rincaro * pesi[2], 4);
                        future[i] += test(pesi[2], 3);
                    }
                    for (int l = 0; l < length; l++) {
                        if (copiedBoard.getCellOccupant(height - 1, l) == 0) {
                            lmoved = true;
                            copiedBoard.move(l, 1);
                            future[i] += test(rincaro * pesi[3], 4);
                            future[i] += test(pesi[3], 3);
                        }
                        if (lmoved) {
                            copiedBoard.undo();
                            lmoved = false;
                        }
                    }
                    if (kmoved) {
                        copiedBoard.undo();
                        kmoved = false;
                    }
                }
                if (jmoved) {
                    copiedBoard.undo();
                    jmoved = false;
                }
            }
            if (imoved) {
                copiedBoard.undo();
                imoved = false;
            }
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

    private int test(int weight, int line) {
        int victoryP = 0;
        victoryP += (copiedBoard.scanHorizontal(line) != 0 ? weight : 0);
        victoryP += (copiedBoard.scanVertical(line) != 0 ? weight : 0);
        victoryP += (copiedBoard.scanMainDiag(line) != 0 ? weight : 0);
        victoryP += (copiedBoard.scanBackDiag(line) != 0 ? weight : 0);
        return victoryP;
    }
}