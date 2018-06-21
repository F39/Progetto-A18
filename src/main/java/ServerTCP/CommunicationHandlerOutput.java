package ServerTCP;

import Controllers.GameControllerInt;
import Utils.AbstractCommand;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class CommunicationHandlerOutput implements Runnable {
    private Socket socket;
    private ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    private GameControllerInt gameControllerInt;


    public CommunicationHandlerOutput(Socket socket, GameControllerInt gameControllerInt) throws IOException {
        this.socket = socket;
        this.gameControllerInt = gameControllerInt;
    }

    @Override
    public void run() {
        List<AbstractCommand> commands = gameControllerInt.getCommandsOut();
        while (true) {

            if (gameControllerInt.getCommandsOut().size() > 0) {
                try {
                    objectOutputStream.writeObject(commands.get(0));
                    gameControllerInt.deleteCommandOut(commands.get(0));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
