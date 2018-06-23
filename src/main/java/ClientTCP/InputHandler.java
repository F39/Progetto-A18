package ClientTCP;

import GameLogic.MatchFlowState;
import GameLogic.Mode;
import Utils.CommandMove;
import Utils.CommandNewGame;
import Utils.CommandPause;
import Utils.CommandQuit;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

public class InputHandler implements Runnable {
    private BufferedReader inStream;
    private Client client;
    private String username, httpHost;
    private int gameId;
    private int myTurn;
    private int turn;
    private MatchFlowState matchFlowState;
    private int port;
    private Console cnsl;

    InputHandler(Client client, String httpHost, int port) {
        inStream = new BufferedReader(new InputStreamReader(System.in));
        this.client = client;
        this.httpHost = httpHost;
        this.port = port;
        this.cnsl = System.console();
    }

    @Override
    public void run() {
        String temp;
        while (true) {
            try {
                if (!client.isLogged()) {
                    System.out.println("username:");
                    username = inStream.readLine();
                    char[] password = cnsl.readPassword("password:\n");

                    String payLoadLogin = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, String.valueOf(password));
                    try {
                        URI address = new URI("http", null, httpHost, port, "/rest/user/loginTCP", null, null);
                        HttpUriRequest request = new HttpPost(address);
                        request.setHeader("Content-Type", "application/json");
                        StringEntity se = null;
                        se = new StringEntity(payLoadLogin);
                        ((HttpPost) request).setEntity(se);
                        HttpResponse response = HttpClientBuilder.create().build().execute(request);
                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            HttpEntity entity = response.getEntity();
                            String responseString = EntityUtils.toString(entity, "UTF-8");
                            JSONObject jsonResponse = new JSONObject(responseString);
                            client.setToken(jsonResponse.getString("token"));
                            client.setLogged(true);
                            System.out.println("Login successful!");
                            System.out.print(">> ");
                        }
                        else {
                            System.out.println("Wrong credentials!");
                        }
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                    client.setUsername(username);
                } else {
                    temp = inStream.readLine();
                    switch (temp.split(" ")[0]) {
                        case "newgame": {
                            client.getMessagesOut().add(new CommandNewGame(username, Mode.valueOf(Integer.parseInt(temp.split(" ")[1]))));
                            break;
                        }
                        case "move": {
                            if (myTurn == turn && matchFlowState != MatchFlowState.paused) {
                                client.getMessagesOut().add(new CommandMove(username, gameId, Integer.parseInt(temp.split(" ")[1])));
                                client.getBoard().move(Integer.parseInt(temp.split(" ")[1]), myTurn);
                                turn -= Math.pow(-1, turn);
                            } else {
                                System.out.println("Wait your turn");
                            }
                            break;
                        }
                        case "pause": {
                            client.getMessagesOut().add(new CommandPause(username, gameId));
                            break;
                        }
                        case "logout": {
                            if (client.isInGame()) {
                                System.out.println("You lose!");
                                client.getMessagesOut().add(new CommandQuit(username, gameId));
                            }
                            client.setQuitFlag(true);
                            break;
                        }
                        case "quit": {
                            client.setInGame(false);
                            client.getMessagesOut().add(new CommandQuit(username, gameId));
                            break;
                        }
                        case "exit": {
                            if (client.isInGame()) {
                                System.out.println("You lose!");
                                client.getMessagesOut().add(new CommandQuit(username, gameId));
                            }
                            client.setQuitFlag(true);
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
        this.myTurn = myTurn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public void setMatchFlowState(MatchFlowState matchFlowState) {
        this.matchFlowState = matchFlowState;
    }
}
