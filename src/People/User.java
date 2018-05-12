package src.People;

import src.GameLogic.Request;

public class User {
    String nickname, email, password;
    boolean waiting;

    public User(String nickname, String email, String password, String confirm) {
        if (password.equals(confirm)) {
            this.nickname = nickname;
            this.email = email;
            this.password = password;
            waiting = false;
        } else
            throw new IllegalArgumentException("Password not correct");
    }

    public void wannaPlay() {
        Request.newRequest(this);
        waiting=true;
    }
}