package ClientTCP;

import Utils.CommandMove;
import Utils.CommandPause;
import Utils.CommandQuit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputHandler implements Runnable{
    private BufferedReader inStream;
    private Client client;
    private String username;
    private int gameId;

    public InputHandler(Client client, String username){
         inStream = new BufferedReader(new InputStreamReader(System.in));
         this.client = client;
         this.username = username;
    }

    @Override
    public void run() {
        String temp;
        while(true){
            try {
                temp = inStream.readLine();
                switch (temp.split(" ")[0]){
                    case "move":{
                        client.getMessagesOut().add(new CommandMove(username, gameId, Integer.parseInt(temp.split(" ")[1])));
                    }
                    case "pause":{
                        client.getMessagesOut().add(new CommandPause(username, gameId));
                    }
                    case "quit":{
                        client.getMessagesOut().add(new CommandQuit(username, gameId));
                    }
                    default:
                        System.out.println("Input error");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
}
