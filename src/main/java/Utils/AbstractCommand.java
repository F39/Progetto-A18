package Utils;

import Controllers.GameController;
import Controllers.UserController;
import DatabaseManagement.User;

public abstract class AbstractCommand {

    private String username;

    public AbstractCommand(){}

    public AbstractCommand(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public User getUser(){
        for (User userOnline: UserController.getOnline().values()) {
            if(getUsername().equals(userOnline.getUsername())){
                return userOnline;
            }
        }
        return null;
    }

    public void setUser(String username) {
        this.username = username;
    }

    public abstract void execute(GameController gameController);

}
