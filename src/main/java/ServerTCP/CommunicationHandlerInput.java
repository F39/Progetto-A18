package ServerTCP;

import Controllers.GameControllerInt;
import Logger.Logger;
import Utils.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

public class CommunicationHandlerInput implements Runnable {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private GameControllerInt gameControllerInt;
    private Map<Socket, String> connectionMap;
    private Logger logger;

    public CommunicationHandlerInput(Socket socket, GameControllerInt gameControllerInt, Map connectionMap) throws IOException {
        this.socket = socket;
        this.connectionMap = connectionMap;
        this.gameControllerInt = gameControllerInt;
        this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        logger = Logger.getInstance();
    }

    @Override
    public void run() {
        AbstractCommand message;
        try {
            while ((message = (AbstractCommand) objectInputStream.readObject()) != null) {
                if (message instanceof CommandNewGame) {
                    CommandNewGame command = new CommandNewGame((CommandNewGame) message);
                    connectionMap.put(socket, command.getUsername());
                    gameControllerInt.newGame(command);
                } else if (message instanceof CommandMove) {
                    CommandMove command = new CommandMove((CommandMove) message);
                    gameControllerInt.move(command);
                } else if (message instanceof CommandPause) {
                    CommandPause command = new CommandPause((CommandPause) message);
                    gameControllerInt.pause(command);
                } else if (message instanceof CommandQuit) {
                    CommandQuit command = new CommandQuit((CommandQuit) message);
                    gameControllerInt.quit(command);
                }

            }
            socket.close();
        } catch (SocketException socketException) {
            if (socketException.toString().contains("Socket closed") || socketException.toString().contains("Connection reset") || socketException.toString().contains("Broken pipe")) {
                logger.log("Socket connection closed");
            } else {
                socketException.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
