package ClientTCP;

import GameLogic.Board;
import Utils.AbstractCommand;
import Utils.CommandOut;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client implements Runnable{
    private List<AbstractCommand> messagesOut;
    private List<CommandOut> messagesIn;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Board board;

    public Client(Board board, String serverIp) throws IOException {
        messagesOut = new ArrayList<>();
        messagesIn = new ArrayList<>();
        socket = new Socket(serverIp, 9000);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.board = board;

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

    public Socket getSocket() {
        return socket;
    }

    public ObjectInputStream getObjectInputStream() {
        return objectInputStream;
    }

    public ObjectOutputStream getObjectOutputStream() {
        return objectOutputStream;
    }

    @Override
    public void run() {
        CommandOut message;
        try{
            while(( message = (CommandOut) objectInputStream.readObject()) != null){
                messagesIn.add(message);
            }
            socket.close();
        }catch(IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
