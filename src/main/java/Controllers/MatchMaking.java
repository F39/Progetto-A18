package Controllers;

import DatabaseManagement.User;
import GameLogic.Mode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MatchMaking implements Runnable {

    private List<User> pendingUsers;
    private GameController gameController;

    public MatchMaking(GameController gameController) {
        this.gameController = gameController;
        this.pendingUsers = new ArrayList<>();
    }

    public void putPendingUsers(User user) {
        this.pendingUsers.add(user);
    }

    @Override
    public void run() {
        int indexP1, indexP2;
        while (true) {
            if (pendingUsers.size() > 2) {
                while (true) {
                    // TODO : define a better match-making algorithm
                    indexP1 = ThreadLocalRandom.current().nextInt(0, pendingUsers.size() + 1);
                    indexP2 = ThreadLocalRandom.current().nextInt(0, pendingUsers.size() + 1);
                    if (indexP1 != indexP2) {
                        gameController.createNewGame(Mode.MultiPlayer, pendingUsers.get(indexP1), pendingUsers.get(indexP2));
                        pendingUsers.remove(indexP1);
                        pendingUsers.remove(indexP2);
                        break;
                    }
                }
            }
        }
    }

}
