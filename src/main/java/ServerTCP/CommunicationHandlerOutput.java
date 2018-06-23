package ServerTCP;

import Controllers.GameControllerInt;
import Utils.AbstractCommand;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CommunicationHandlerOutput implements Runnable {
    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private GameControllerInt gameControllerInt;
    private Map<Socket, String> connectionMap;

    public CommunicationHandlerOutput(Socket socket, GameControllerInt gameControllerInt, Map connectionMap) throws IOException {
        this.socket = socket;
        this.connectionMap = connectionMap;
        this.gameControllerInt = gameControllerInt;
        this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        List<AbstractCommand> commands = gameControllerInt.getCommandsOut();
        while (true) {
            if (gameControllerInt.getCommandsOut().size() > 0) {
                String username = connectionMap.get(socket);
                try {
                    Iterator<AbstractCommand> iterator = commands.iterator();
                    while(iterator.hasNext()){
                        AbstractCommand command = iterator.next();
                        if(command.getUsername().equals(username)){
                            objectOutputStream.writeObject(command);
                            iterator.remove();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
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
