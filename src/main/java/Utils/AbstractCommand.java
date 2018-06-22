package Utils;

import Controllers.UserController;
import GameLogic.Player;

import java.io.Serializable;

public abstract class AbstractCommand implements Serializable {

    private String username;
    Player player;

    public AbstractCommand() {
    }

    public AbstractCommand(String username) {
        this.username = username;
        for (Player userOnline : UserController.getOnline().values()) {
            if (username.equals(userOnline.getUsername())) {
                player = userOnline;
            }
        }

    }

    public void setUsername(String username) {
        this.username = username;
        for (Player userOnline : UserController.getOnline().values()) {
            if (username.equals(userOnline.getUsername())) {
                player = userOnline;
            }
        }
    }

    public String getUsername() {
        return username;
    }

    public Player getPlayer() {
        return player;
    }

    public void setUser(String username) {
        this.username = username;
    }

    public abstract void execute();

}
