package Controllers;

import GameLogic.Mode;
import GameLogic.Player;
import Logger.Logger;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Match making thread, it handles the game pending users and matches it with the first available one
 */
public class MatchMaking implements Runnable {

    private Map<Player, Mode> pendingPlayers;
    private GameController gameController;
    private Logger logger;

    public MatchMaking(GameController gameController) {
        this.gameController = gameController;
        this.pendingPlayers = new HashMap<>();
        logger = Logger.getInstance();
    }

    public void putPendingUsers(Player player, Mode mode) {
        this.pendingPlayers.put(player, mode);
    }

    @Override
    public void run() {
        int indexP1, indexP2;
        logger.log("Started Matchmaking thread");
        while (true) {
            if (pendingPlayers.size() >= 2) {
                while (true) {
                    indexP1 = ThreadLocalRandom.current().nextInt(0, pendingPlayers.size());
                    indexP2 = ThreadLocalRandom.current().nextInt(0, pendingPlayers.size());
                    Set<Player> setPlayer = pendingPlayers.keySet();
                    if (indexP1 != indexP2) {
                        List<Player> listPlayer = new ArrayList<>(setPlayer);
                        Player p1 = listPlayer.get(indexP1);
                        Player p2 = listPlayer.get(indexP2);
                        if(pendingPlayers.get(p1) == pendingPlayers.get(p2)){
                            gameController.createNewMultiPlayerGame(p1, p2, pendingPlayers.get(p1));
                            pendingPlayers.remove(p1);
                            pendingPlayers.remove(p2);
                            logger.log(String.format("Matched a new game: %s and %s will play.", p1.getUsername(), p2.getUsername()));
                            break;
                        }
                    }
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
