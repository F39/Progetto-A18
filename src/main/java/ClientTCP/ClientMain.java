package ClientTCP;

import Utils.CommandOut;

import java.io.IOException;
import java.net.UnknownHostException;

public class ClientMain {
    //TODO:Aggiungere user fittizio lato server al momento della connessione e poi mandare comando new game lato client

    public static void main(String[] args) {
        try {
            Client client = new Client();
            InputHandler inputHandler = new InputHandler(client, "console"+args[0]);
            System.out.println("Connect 4");
            Thread read = new Thread(client);
            Thread write = new Thread(inputHandler);
            read.start();
            write.start();
            boolean refresh = false;

            CommandOut message;
            while (true) {
                refresh = false;
                if(client.getMessagesIn().size() > 0){
                    message = client.getMessagesIn().get(0);
                    if (message.getMove() == -1 && message.getGameId() == 0) {
                        //poll response
                    } else if (message.getMove() == -2 && message.getGameId() != -1) {
                        inputHandler.setGameId(message.getGameId());

                    } else if (message.getMove() == -1) {
                        //Cambio di stato
                    } else {
                        //mossa
                    }
                    client.getMessagesIn().remove(0);
                    refresh = true;
                }
                if(client.getMessagesOut().size() > 0){
                    client.getObjectOutputStream().writeObject(client.getMessagesOut().get(0));
                    client.getMessagesOut().remove(0);
                    refresh = true;
                }
                if(refresh){
                    printBoard();
                }
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static void printBoard() {
        //TODO : Print state
    }
}