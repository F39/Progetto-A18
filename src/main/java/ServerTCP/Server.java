package ServerTCP;

import Controllers.GameControllerInt;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server implements Runnable{
    private ServerSocket serverSocket= new ServerSocket(9000, 10, InetAddress.getLocalHost());
    private ArrayList<CommunicationHandlerInput> connectionsInput = new ArrayList<>();
    private ArrayList<CommunicationHandlerOutput> connectionsOutput = new ArrayList<>();
    private GameControllerInt gameControllerInt;


    public Server(GameControllerInt gameController) throws IOException {
        this.gameControllerInt = gameController;
    }

//    public ArrayList<Socket> getConnections() {
//        return connections;
//    }

    @Override
    public void run() {
        CommunicationHandlerInput communicationHandlerInput;
        CommunicationHandlerOutput communicationHandlerOutput;
        while(true){
            try {
                Socket socket = serverSocket.accept();
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
