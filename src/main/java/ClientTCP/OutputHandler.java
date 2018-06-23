package ClientTCP;

import java.io.IOException;

public class OutputHandler implements Runnable {

    private Client client;

    OutputHandler(Client client) {
        this.client = client;
    }

    @Override
    public void run() {

        while (true) {
            try {
                if (client.getMessagesOut().size() > 0) {
                    client.getObjectOutputStream().writeObject(client.getMessagesOut().get(0));
                    client.getMessagesOut().remove(0);
                }
                Thread.sleep(100);
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}
