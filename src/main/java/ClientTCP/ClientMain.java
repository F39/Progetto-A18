package ClientTCP;

import GameLogic.Board;
import GameLogic.MatchFlowState;
import Utils.CommandOut;

import java.io.IOException;

public class ClientMain {

    public static void main(String[] args) {
        try {
            Board board = new Board();
            Client client = new Client(board, args[0], Integer.parseInt(args[1]));
            InputHandler inputHandler = new InputHandler(client, args[2], Integer.parseInt(args[3]));
            System.out.println("Connect 4");
            Thread read = new Thread(client);
            Thread write = new Thread(inputHandler);
            read.start();
            write.start();
            CommandOut message;

            while (true) {
                if (client.getMessagesIn().size() > 0) {
                    message = client.getMessagesIn().get(0);
                    if (message.getMove() == -2 && message.getGameId() != -1) {
                        // new game
                        inputHandler.setGameId(message.getGameId());
                        int myTurn = message.getMatchFlowState() == MatchFlowState.started1 ? 1 : 2;
                        inputHandler.setMyTurn(myTurn);
                        inputHandler.setTurn(1);
                        System.out.println("Game started");
                        System.out.println(board);
                        System.out.print(">> ");
                        inputHandler.setInGame(true);
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
                                System.out.print(">> ");
                                break;
                            }
                            case looser: {
                                System.out.println("\nYou Lose!");
                                System.out.print(">> ");
                                break;
                            }
                            case tie: {
                                System.out.println("\nIt's a tie!");
                                System.out.print(">> ");
                                break;
                            }
                        }
                    } else {
                        // move
                        board.move(message.getMove(), inputHandler.getTurn());
                        int turn = (int) (inputHandler.getTurn() - Math.pow(-1, inputHandler.getTurn()));
                        inputHandler.setTurn(turn);
                        System.out.println(board);
                        System.out.print(">> ");
                    }
                    client.getMessagesIn().remove(0);
                }
                if (client.getMessagesOut().size() > 0) {
                    client.getObjectOutputStream().writeObject(client.getMessagesOut().get(0));
                    client.getMessagesOut().remove(0);
                }
                if (client.isQuitFlag()) {
                    System.out.println("Bye! :) :)");
                    System.exit(0);
                }
                Thread.sleep(100);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}