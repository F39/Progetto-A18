package GameLogic;

import DatabaseManagement.User;

public class Player {

    private User user;
    private long lastPoll;
    private Mode mode;

    public Player() {
    }

    public Player(User user) {
        this.user = user;
        lastPoll = System.currentTimeMillis();
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
