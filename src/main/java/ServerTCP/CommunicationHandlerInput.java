package ServerTCP;

import Controllers.GameControllerInt;
import Utils.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class CommunicationHandlerInput implements Runnable {
    private Socket socket;
    private ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
    private GameControllerInt gameControllerInt;

    public CommunicationHandlerInput(Socket socket, GameControllerInt gameControllerInt) throws IOException {
        this.socket = socket;
        this.gameControllerInt = gameControllerInt;
    }

    @Override
    public void run() {
        AbstractCommand message;
        try{
            while(( message = (AbstractCommand) objectInputStream.readObject()) != null){
                if(message instanceof CommandNewGame){
                    gameControllerInt.newGame((CommandNewGame) message);
                }else if(message instanceof CommandMove){
                    gameControllerInt.move((CommandMove) message);
                }else if(message instanceof CommandPause){
                    gameControllerInt.pause((CommandPause) message);
                }else if(message instanceof CommandQuit){
                    gameControllerInt.quit((CommandQuit) message);
                }

            }
            socket.close();
        }catch(IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
