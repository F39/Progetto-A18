package ServerTCP;

import Controllers.GameControllerInt;
import Logger.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server implements Runnable {

    private ServerSocket serverSocket;
    private ArrayList<CommunicationHandlerInput> connectionsInput;
    private ArrayList<CommunicationHandlerOutput> connectionsOutput;
    private GameControllerInt gameControllerInt;
    private Map<Socket, String> connectionMap;

    public Server(GameControllerInt gameController) {
        this.gameControllerInt = gameController;
        try {
            this.serverSocket = new ServerSocket(9000, 10, InetAddress.getByName("localhost"));
            this.connectionsOutput = new ArrayList<>();
            this.connectionsInput = new ArrayList<>();
            this.connectionMap = new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        CommunicationHandlerInput communicationHandlerInput;
        CommunicationHandlerOutput communicationHandlerOutput;
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Logger.log("Connection started");
                communicationHandlerOutput = new CommunicationHandlerOutput(socket, gameControllerInt, connectionMap);
                communicationHandlerInput = new CommunicationHandlerInput(socket, gameControllerInt, connectionMap);
                Thread threadOut = new Thread(communicationHandlerOutput);
                Thread threadIn = new Thread(communicationHandlerInput);
                threadIn.start();
                threadOut.start();
                connectionsOutput.add(communicationHandlerOutput);
                connectionsInput.add(communicationHandlerInput);
                Thread.sleep(100);
            }
            catch (IOException | InterruptedException e) {
                    e.printStackTrace();
            }
        }
    }

}
