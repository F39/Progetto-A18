package ClientTCP;

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
    private List<CommandOut> messagesIn = new ArrayList<>();
    private Socket socket = new Socket(InetAddress.getLocalHost(), 9000);
    private ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
    private ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

    public Client() throws IOException {

    }

    public List<AbstractCommand> getMessagesOut() {
        return messagesOut;
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
