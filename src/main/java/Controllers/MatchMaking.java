package Controllers;

import GameLogic.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MatchMaking implements Runnable {

    private List<Player> pendingPlayers;
    private GameController gameController;

    public MatchMaking(GameController gameController) {
        this.gameController = gameController;
        this.pendingPlayers = new ArrayList<>();
    }

    public void putPendingUsers(Player player) {
        this.pendingPlayers.add(player);
    }

    @Override
    public void run() {
        int indexP1, indexP2;
        int c = 0;
        while (true) {
            System.out.println("Thread running + " + (c++));
            if (pendingPlayers.size() >= 2) {
                //System.out.println("Pending at least 2 player ");
                while (true) {
                    // TODO : define a better match-making algorithm

                    indexP1 = ThreadLocalRandom.current().nextInt(0, pendingPlayers.size());
                    indexP2 = ThreadLocalRandom.current().nextInt(0, pendingPlayers.size());
                    if (indexP1 != indexP2) {
                        Player p1 = pendingPlayers.get(indexP1);
                        Player p2 = pendingPlayers.get(indexP2);
                        gameController.createNewMultiPlayerGame(p1, p2);
                        pendingPlayers.remove(p1);
                        pendingPlayers.remove(p2);
                        System.out.println("Matchmakato " + p1.getUsername() + " " + p2.getUsername());
                        break;
                    }
                }
            }
        }
    }

}
