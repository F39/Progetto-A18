package ClientTCP;

import GameLogic.Board;
import Utils.AbstractCommand;
import Utils.CommandOut;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client implements Runnable {
    private List<AbstractCommand> messagesOut;
    private List<CommandOut> messagesIn;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Board board;
    private boolean quitFlag;

    public Client(Board board, String serverIp, int port) throws IOException {
        messagesOut = new ArrayList<>();
        messagesIn = new ArrayList<>();
        socket = new Socket(serverIp, port);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.board = board;
        this.quitFlag = false;
    }

    public List<AbstractCommand> getMessagesOut() {
        return messagesOut;
    }

    public Board getBoard() {
        return board;
    }

    public List<CommandOut> getMessagesIn() {
        return messagesIn;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    @Override
    public void run() {
        CommandOut message;
        try {
            while ((message = (CommandOut) objectInputStream.readObject()) != null) {
                messagesIn.add(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        finally {
            if(socket.isConnected()){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public boolean isQuitFlag() {
        return quitFlag;
    }

    public void setQuitFlag(boolean flag) {
        this.quitFlag = flag;
    }

}
