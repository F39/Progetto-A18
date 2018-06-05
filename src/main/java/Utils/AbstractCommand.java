package Utils;

import DatabaseManagement.User;

public abstract class AbstractCommand {

    private User user;

    public AbstractCommand(){}

    public AbstractCommand(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
