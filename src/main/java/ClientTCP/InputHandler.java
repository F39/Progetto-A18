package ClientTCP;

import GameLogic.Match;
import GameLogic.MatchFlowState;
import GameLogic.Mode;
import Utils.CommandMove;
import Utils.CommandNewGame;
import Utils.CommandPause;
import Utils.CommandQuit;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class InputHandler implements Runnable{
    private BufferedReader inStream;
    private Client client;
    private String username, password;
    private int gameId;
    private boolean logged;
    private int myTurn;
    private int turn;
    private MatchFlowState matchFlowState;

    public InputHandler(Client client){
         inStream = new BufferedReader(new InputStreamReader(System.in));
         this.client = client;
         this.logged = false;
    }

    @Override
    public void run() {
        String temp;
        while(true){
            try {
                if(!logged){
                    System.out.println("username:");
                    username = inStream.readLine();
                    System.out.println("password:");
                    password = inStream.readLine();
                    String payLoadLogin = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
                    try {
                        URI address = new URI("http", null, "localhost", 8080, "/rest/user/loginTCP", null, null);
                        HttpUriRequest request = new HttpPost(address);
                        request.setHeader("Content-Type", "application/json");
                        StringEntity se = null;
                        se = new StringEntity(payLoadLogin);
                        ((HttpPost) request).setEntity(se);
                        HttpResponse response = HttpClientBuilder.create().build().execute(request);
                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
                            logged = true;
                            System.out.println("Login successfully");
                        }
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    temp = inStream.readLine();
                    switch (temp.split(" ")[0]){
                        case "newgame":{
                            client.getMessagesOut().add(new CommandNewGame(username, Mode.valueOf(Integer.parseInt(temp.split(" ")[1]))));
                            break;
                        }
                        case "move":{
                            if(myTurn == turn && matchFlowState != MatchFlowState.paused){
                                client.getMessagesOut().add(new CommandMove(username, gameId, Integer.parseInt(temp.split(" ")[1])));
                                client.getBoard().move(Integer.parseInt(temp.split(" ")[1]), myTurn);
                                turn -= Math.pow(-1, turn);
                            }
                            else{
                                System.out.println("Wait your turn");
                            }
                            break;
                        }
                        case "pause":{
                            client.getMessagesOut().add(new CommandPause(username, gameId));
                            break;
                        }
                        case "quit":{
                            client.getMessagesOut().add(new CommandQuit(username, gameId));
                            break;
                        }
                        default:
                            System.out.println("Input error");
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public int getTurn() {
        return turn;
    }

    public void setMyTurn(int myTurn) {
        this.myTurn =  myTurn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void setMatchFlowState(MatchFlowState matchFlowState) {
        this.matchFlowState = matchFlowState;
    }
}
