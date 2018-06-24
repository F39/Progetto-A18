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
    private boolean logged;
    private boolean inGame;
    private String username;
    private String token;

    public Client(Board board, String serverIp, int port) throws IOException {
        messagesOut = new ArrayList<>();
        messagesIn = new ArrayList<>();
        socket = new Socket(serverIp, port);
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());
        this.board = board;
        this.quitFlag = false;
        this.logged = false;
        this.inGame = false;
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
        } finally {
            if (socket.isConnected()) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public boolean isQuitFlag() {
        return quitFlag;
    }

    public void setQuitFlag(boolean flag) {
        this.quitFlag = flag;
    }

    public boolean isLogged() {
        return this.logged;
    }

    public void setLogged(boolean logged) {
        this.logged = logged;
    }

    public boolean isInGame() {
        return inGame;
    }

    public void setInGame(boolean inGame) {
        this.inGame = inGame;
    }

    public void setBoard(Board board) {
        this.board = board;
    }
}
