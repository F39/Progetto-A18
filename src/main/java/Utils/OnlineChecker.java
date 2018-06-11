package Utils;

import Controllers.UserController;

public class OnlineChecker implements Runnable {

    @Override
    public void run() {
        while (true) {
            UserController.removeOfflinePlayers();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
