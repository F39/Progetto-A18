package GameLogic;

import DatabaseManagement.User;

public class Player {

    private User user;

    public Player() {
    }

    public Player(User user) {
        this.user = user;
    }

    public Player(Mode mode){

    }

    public User getUser() {
        return user;
    }
}
