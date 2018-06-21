package ClientTCP;

import Utils.CommandOut;

import java.io.IOException;

public class ClientMain {

    // TODO: Aggiungere user fittizio lato server al momento della connessione e poi mandare comando new game lato client

    public static void main(String[] args) {
        try {
            Client client = new Client();
            InputHandler inputHandler = new InputHandler(client, "console" + args[0]);
            System.out.println("Connect 4");
            Thread read = new Thread(client);
            Thread write = new Thread(inputHandler);
            read.start();
            write.start();
            boolean refresh = false;

            CommandOut message;
            while (true) {
                refresh = false;
                if (client.getMessagesIn().size() > 0) {
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
                if (client.getMessagesOut().size() > 0) {
                    client.getObjectOutputStream().writeObject(client.getMessagesOut().get(0));
                    client.getMessagesOut().remove(0);
                    refresh = true;
                }
                if (refresh) {
                    printBoard();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void printBoard() {
//        int l= b.getLength();
//        int h= b.getHeight();
//        System.out.println("\nFORZA4");
//        StringBuilder sb = new StringBuilder();
//        sb.append("\t");
//        for (int i = 0; i < l; i++)
//            sb.append("\t" + i);
//        System.out.println(sb.toString() + "\n");
//        sb.delete(0, sb.length());
//        for (int i = h - 1; i >= 0; i--) {
//            sb.append(i + "\t\t");
//            for (int j = 0; j < l; j++) {
//                switch (b.getCellOccupant(i, j)) {
//                    case 1:
//                        sb.append("X\t");
//                        break;
//                    case 2:
//                        sb.append("O\t");
//                        break;
//                    default:
//                        sb.append("-\t");
//                }
//            }
//            System.out.println(sb.toString());
//            sb.delete(0, sb.length());
//        }
    }
}