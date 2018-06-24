package ClientTCP;

import GameLogic.Board;
import GameLogic.MatchFlowState;
import Utils.CommandOut;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static java.lang.Integer.parseInt;

public class ClientMain {

    public static void main(String[] args) {
        try {
            Board board = new Board();
            Client client = new Client(board, args[0], parseInt(args[1]));
            String httpHost = args[2];
            int port = Integer.parseInt(args[3]);
            InputHandler inputHandler = new InputHandler(client, httpHost, port);
            OutputHandler outputHandler = new OutputHandler(client);
            System.out.println("Connect 4");
            Thread socketThread = new Thread(client);
            Thread threadIn = new Thread(inputHandler);
            Thread threadOut = new Thread(outputHandler);
            socketThread.start();
            threadIn.start();
            threadOut.start();

            CommandOut message;

            while (true) {
                if (client.isQuitFlag()) {
                    String payLoadLogout = String.format("{\"username\":\"%s\", \"token\":\"%s\"}", client.getUsername(), client.getToken());
                    try {
                        URI address = new URI("http", null, httpHost, port, "/rest/user/logout", null, null);
                        HttpUriRequest request = new HttpPost(address);
                        request.setHeader("Content-Type", "application/json");
                        StringEntity se = null;
                        se = new StringEntity(payLoadLogout);
                        ((HttpPost) request).setEntity(se);
                        HttpResponse response = HttpClientBuilder.create().build().execute(request);
                        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                            client.setLogged(false);
                            System.out.println("Logout successful!");
                        }
                    } catch (IOException | URISyntaxException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Bye! :) :)");
                    System.exit(0);
                }
                if (client.getMessagesIn().size() > 0) {
                    message = client.getMessagesIn().get(0);
                    if (message.getMove() == -2 && message.getGameId() != -1) {
                        // new game
                        client.setBoard(board);
                        inputHandler.setGameId(message.getGameId());
                        int myTurn = message.getMatchFlowState() == MatchFlowState.started1 ? 1 : 2;
                        inputHandler.setMyTurn(myTurn);
                        inputHandler.setTurn(1);
                        System.out.println("Game started");
                        System.out.println(board);
                        System.out.print(">> ");
                        client.setInGame(true);
                    } else if (message.getMove() == -1) {
                        // game flow state change
                        inputHandler.setMatchFlowState(message.getMatchFlowState());
                        switch (message.getMatchFlowState()) {
                            case paused: {
                                System.out.println("\nPaused");
                                System.out.print(">> ");
                                break;
                            }
                            case resumed: {
                                System.out.println("\nResumed");
                                System.out.print(">> ");
                                break;
                            }
                            case winner: {
                                System.out.println("\nYou Win!");
                                board = new Board();
                                client.setInGame(false);
                                System.out.print(">> ");
                                break;
                            }
                            case looser: {
                                System.out.println("\nYou Lose!");
                                board = new Board();
                                client.setInGame(false);
                                System.out.print(">> ");
                                break;
                            }
                            case tie: {
                                System.out.println("\nIt's a tie!");
                                board = new Board();
                                client.setInGame(false);
                                System.out.print(">> ");
                                break;
                            }
                        }
                    } else if(client.isInGame()){
                        // move
                        board.move(message.getMove(), inputHandler.getTurn());
                        int turn = (int) (inputHandler.getTurn() - Math.pow(-1, inputHandler.getTurn()));
                        inputHandler.setTurn(turn);
                        System.out.println(board);
                        System.out.print(">> ");
                    }
                    client.getMessagesIn().remove(0);
                }
                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}