package ServerTCP;

import Controllers.GameController;

public class ServerMain {

    public static void main(String[] args) {

        GameController gameController = new GameController();
        Server server = new Server(gameController);
        Thread serverThread = new Thread(server);
        serverThread.start();

    }

}
