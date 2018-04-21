package ArtificialAgent;

import GameLogic.*;

import java.util.ArrayList;

public class ArtificialLev1 {
    Connect4Game game;
    Board mirror;
    ArrayList<Integer> avoid;

    public ArtificialLev1(Connect4Game game) {
        this.game = game;
        mirror = game.getBoard();
        avoid = new ArrayList<>();
    }

    public void move() {
        boolean done = false;
        mirror = game.getBoard();
        int col = getInTheWay();
        if (col >= 0 && col <= mirror.getLength() - 1 && mirror.getCellOccupant(mirror.getHeight() - 1, col) <= 0)
            game.move(col);
        else { // se non c'è possibilità di tre in fila da parte di giocatore 1
            double scale = 2;
            for (int count = 0; count < 100 && !done; count++) { //per 100 volte prova a piazzarlo vicino
                int i = (int) ((Math.random() - 0.5) * mirror.getLength() / scale);
                i += mirror.getLastC();
                if (count % 10 == 0)
                    scale -= 0.15;
                if (!(avoid.contains(i)) && mirror.getCellOccupant(mirror.getHeight() - 1, i) == 0) {
                    game.move(i);
                    done = true;
                }
            }
            if (!done) { // se non ci è riuscito, scan di tutte le colonne per trovarne una libera dalla successiva
                for (int i = 0; i < mirror.getLength() && !done; i++) {
                    int ii = (i + 1 + mirror.getLastC()) % mirror.getLength();
                    if (!(avoid.contains(ii)) && mirror.getCellOccupant(mirror.getHeight() - 1, ii) == 0) {
                        game.move(ii);
                        done = true;
                    }
                }
            }
        }
    }

    public int getInTheWay() {
        int lastC = mirror.getLastC();
        if (mirror.getMoveNo() <= 2) {
            int guess;
            if ((guess = (int) (Math.random() * 3 + lastC - 1)) < 0)
                guess++;
            else if ((guess = (int) (Math.random() * 3 + lastC - 1)) > mirror.getLength() - 1)
                guess--;
            return guess;
        } else
            return superScan(mirror.getLastR(), mirror.getLastC(), 1, 3);
    }

    public int superScan(int r, int c, int player, int line) {
        int counter = 0;
        int i, j;
        for (j = 0; j < mirror.getLength(); j++) { //row check
            if (mirror.getCellOccupant(r, j) == player)
                counter++;
            else
                counter = 0;
            if (counter == line)
                return canHeWin(r, j - line + 1, r, j);
        }
        counter = 0;
        for (i = 0; i <= r; i++) { //column check
            if (mirror.getCellOccupant(i, c) == player)
                counter++;
            else
                counter = 0;
            if (counter == line) {
                return canHeWin(i - line + 1, c, i, c);
            }
        }
        counter = 0;
        if (r - c <= 0) {
            i = 0;
            j = c - r;
        } else {
            j = 0;
            i = r - c;
        }
        for (; i < mirror.getHeight() && j < mirror.getLength(); i++, j++) {   //diagonal forward check (/)
            if (mirror.getCellOccupant(i, j) == player)
                counter++;
            else
                counter = 0;
            if (counter == line)
                return canHeWin(i - line + 1, j - line + 1, i, j);
        }
        counter = 0;
        if (r + c <= mirror.getLength() - 1) {
            i = 0;
            j = r + c;
        } else {
            i = r + c - mirror.getLength() + 1;
            j = mirror.getLength() - 1;
        }
        for (; i < mirror.getHeight() && j >= 0; i++, j--) {   //diagonal backward check (\)
            if (mirror.getCellOccupant(i, j) == player)
                counter++;
            else
                counter = 0;
            if (counter == line)
                return canHeWin(i - line + 1, j + line - 1, i, j);
        }
        return -1;
    }

    public int canHeWin(int r1, int c1, int r2, int c2) { //inizio, fine (sempre verso l'alto)
        avoid.clear();
        if (r2 - r1 == 0) {////////////////////////////////////////////////////////////////////////check the row
            if ((mirror.getCellOccupant(r1, c1 - 1)) == 0) {
                if (mirror.getCellOccupant(r1 - 1, c1 - 1) != 0)
                    return c1 - 1; //impedisce di fare 4 orizzontale verso sx
                else if (mirror.getCellOccupant(r1 - 2, c1 - 1) != 0)
                    avoid.add(c1 - 1); // evita l'appoggio orizzontale sx
            }
            if ((mirror.getCellOccupant(r1, c2 + 1)) == 0) {
                if (mirror.getCellOccupant(r1 - 1, c2 + 1) != 0)
                    return c2 + 1; //impedisce di fare 4 orizzontale verso dx
                else if (mirror.getCellOccupant(r1 - 2, c2 + 1) != 0)
                    avoid.add(c2 + 1); // evita l'appoggio orizzontale dx
            }
        } else if (c2 - c1 == 0) {///////////////////////////////////////////////////////////////check the column
            return c1; //impedisce di fare 4 verticale
        } else if ((r2 - r1) * (c2 - c1) < 0) {/////////////////////////////////////// check the backward diagonal(\)
            if ((mirror.getCellOccupant(r2 + 1, c2 - 1)) == 0) {
                if (mirror.getCellOccupant(r2, c2 - 1) != 0)
                    return c2 - 1; //impedisce di fare 4 diagonale verso sx
                else if (mirror.getCellOccupant(r2 -1, c2 - 1) != 0)
                    avoid.add(c2 - 1); // evita l'appoggio diagonale sx
            }
            if ((mirror.getCellOccupant(r1 - 1, c1 + 1)) == 0) {
                if (mirror.getCellOccupant(r1 - 2, c1 + 1) != 0)
                    return c1 + 1; //impedisce di fare 4 diagonale verso dx
                else if (mirror.getCellOccupant(r1 - 3, c1 + 1) != 0)
                    avoid.add(c1+ 1); // evita l'appoggio diagonale dx
            }
        } else { ///////////////////////////////////////////////////////////////////// check the forward diagonal(/)
            if ((mirror.getCellOccupant(r2 + 1, c2 + 1)) == 0) {
                if (mirror.getCellOccupant(r2, c2 + 1) != 0)
                    return c2 + 1; //impedisce di fare 4 diagonale verso dx
                else if (mirror.getCellOccupant(r2 - 1, c2 + 1) != 0)
                    avoid.add(c2 + 1); // evita l'appoggio diagonale dx
            }
            if ((mirror.getCellOccupant(r1 - 1, c1 - 1)) == 0) {
                if (mirror.getCellOccupant(r1 - 2, c1 - 1) != 0)
                    return c1 - 1; //impedisce di fare 4 diagonale verso sx
                else if (mirror.getCellOccupant(r1 - 3, c1 - 1) != 0)
                    avoid.add(c1 - 1); // evita l'appoggio diagonale sx
            }
        }
        return -1; //non c'è rischio di (line+1) in fila
    }
}