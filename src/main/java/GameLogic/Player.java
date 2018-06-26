package GameLogic;

import DatabaseManagement.User;

import java.io.Serializable;

/**
 * Class representing the player of the game
 */
public class Player implements Serializable {

    private User user;
    private long lastPoll;
    private boolean hasToPoll;
    private Mode mode;

    public Player() {
    }

    public boolean hasToPoll() {
        return hasToPoll;
    }

    public void setHasToPoll(boolean hasToPoll) {
        this.hasToPoll = hasToPoll;
    }

    public Player(User user) {
        this.user = user;
        lastPoll = System.currentTimeMillis();
        hasToPoll = true;

    }

    public Player(Mode mode){
        this.mode = mode;
    }

    public void poll(){
        lastPoll = System.currentTimeMillis();
    }

    public long getLastPoll() {
        return lastPoll;
    }

    public String getUsername(){
        return user.getUsername();
    }

    public User getUser() {
        return user;
    }

    public Mode getMode() {
        return mode;
    }
}
