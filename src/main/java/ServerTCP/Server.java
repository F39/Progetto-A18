package ServerTCP;

import Controllers.GameControllerInt;
import Logger.Logger;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable {

    private ServerSocket serverSocket;
    private ArrayList<CommunicationHandlerInput> connectionsInput;
    private ArrayList<CommunicationHandlerOutput> connectionsOutput;
    private GameControllerInt gameControllerInt;

    public Server(GameControllerInt gameController) {
        this.gameControllerInt = gameController;
        try {
            this.serverSocket = new ServerSocket(9000, 10, InetAddress.getLocalHost());
            this.connectionsOutput = new ArrayList<>();
            this.connectionsInput = new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public ArrayList<Socket> getConnections() {
//        return connections;
//    }

    @Override
    public void run() {
        CommunicationHandlerInput communicationHandlerInput;
        CommunicationHandlerOutput communicationHandlerOutput;
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Logger.log("Connection started");
                communicationHandlerInput = new CommunicationHandlerInput(socket, gameControllerInt);
                communicationHandlerOutput = new CommunicationHandlerOutput(socket, gameControllerInt);
                Thread threadIn = new Thread(communicationHandlerInput);
                Thread threadOut = new Thread(communicationHandlerOutput);
                threadIn.start();
                threadOut.start();
                connectionsInput.add(communicationHandlerInput);
                connectionsOutput.add(communicationHandlerOutput);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
