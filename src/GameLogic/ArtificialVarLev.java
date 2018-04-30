package src.GameLogic;

/**
 * Implementation of an artificial player
 */
public class ArtificialVarLev {
    private Connect4Game game;
    private Board mirror;
    private int lenght, height;
    private int[] futur1;
    private int[] pesi;
    //int me = 2, player = 1;

    /**
     * Create a new artificial player for the specified game, with the level of difficulty specified by the parameters a, b, c, d, representing the weights of the 4-move horizon
     */
    public ArtificialVarLev(Connect4Game game, int a, int b, int c, int d) {
        this.game = game;
        mirror = game.getBoard();
        lenght = mirror.getLength();
        height = mirror.getHeight();
        futur1 = new int[lenght];
        pesi = new int[]{a, b, c, d};
    }

    /**
     * Algorithm for the choice of the best move to do, checking all the possible scenarios of the next 4 moves
     */
    public void move() {
        int guess;
        if (mirror.getMoveNo() <= 2) {
            if ((guess = (int) (Math.random() * 3 + mirror.getLastC() - 1)) < 0)
                guess++;
            else if ((guess = (int) (Math.random() * 3 + mirror.getLastC() - 1)) > mirror.getLength() - 1)
                guess--;
            game.move(guess); //prime due mosse
        } else {
            guess = forcast();
            if (mirror.getCellOccupant(height - 1, guess) == 0)
                game.move(guess); //uso della previsione
            else {
                game.move((int) (Math.random() * lenght)); //previsione fallita
            }
        }
    }

    private int forcast() { //muove con orizzonte di predizione 4
        boolean imoved = false, jmoved = false, kmoved = false, lmoved = false;
        for (int i = 0; i < lenght; i++)
            futur1[i] = 0;
        for (int i = 0; i < lenght; i++) {
            mirror = game.getBoard();
            try {
                if (mirror.getCellOccupant(height - 1, i) == 0) {
                    imoved = true;
                    mirror.move(i);
                    futur1[i] = test(i, pesi[0]);
                } else
                    futur1[i] = -1000; // è full column
            } catch (RuntimeException e) { // mossa vincente
                if (imoved)
                    mirror.undo();
                return i;
            }
            for (int j = 0; j < lenght; j++) {
                try {
                    if (mirror.getCellOccupant(height - 1, j) == 0) {
                        jmoved = true;
                        mirror.move(j);
                        futur1[i] += test(j, pesi[1]);
                    }
                } catch (Exception e) {
                    futur1[i] += test(j, pesi[1]);
                }
                for (int k = 0; k < lenght; k++) {
                    try {
                        if (mirror.getCellOccupant(height - 1, k) == 0) {
                            kmoved = true;
                            mirror.move(k);
                            futur1[i] += deeptest(k, pesi[2]);
                        }
                    } catch (Exception e) {
                        futur1[i] += deeptest(k, pesi[2]);
                    }
                    for (int l = 0; l < lenght; l++) {
                        try {
                            if (mirror.getCellOccupant(height - 1, l) == 0) {
                                lmoved = true;
                                mirror.move(l);
                                futur1[i] += deeptest(l, pesi[3]);
                            }
                        } catch (Exception e) {
                            futur1[i] += deeptest(l, pesi[3]);
                        }
                        if (lmoved)
                            mirror.undo();
                        lmoved = false;
                    }
                    if (kmoved)
                        mirror.undo();
                    kmoved = false;
                }
                if (jmoved)
                    mirror.undo();
                jmoved = false;
            }
            if (imoved)
                mirror.undo();
            imoved = false;
        }
        int max = -2000;
        for (int i = 0; i < lenght; i++) {
            if (futur1[i] > max)
                max = futur1[i];
        }
        for (int i = ((int) (Math.random() * lenght)), count = 0; count < lenght; count++, i = (i + 1) % lenght) {
            if (futur1[i] == max && max > -1000) {
                return i;
            }
        }
        return -1;
    }

    private int test(int c, int peso) {
        int vittorie = 0;
        vittorie += (mirror.scanHorizontal(c, 4) != 0 ? 3 * peso : 0);
        vittorie += (mirror.scanVertical(c, 4) != 0 ? 3 * peso : 0);
        vittorie += (mirror.scanForDiag(c, 4) != 0 ? 3 * peso : 0);
        vittorie += (mirror.scanBacDiag(c, 4) != 0 ? 3 * peso : 0);

        return vittorie;
    }

    private int deeptest(int c, int peso) {
        int vittorie = test(c, peso);
        vittorie += mirror.scanHorizontal(c, 3) != 0 ? peso : 0;
        vittorie += mirror.scanVertical(c, 3) != 0 ? peso : 0;
        vittorie += mirror.scanForDiag(c, 3) != 0 ? peso : 0;
        vittorie += mirror.scanBacDiag(c, 3) != 0 ? peso : 0;
        return vittorie;
    }
}