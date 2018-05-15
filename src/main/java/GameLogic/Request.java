package GameLogic;

import People.User;

import java.util.ArrayList;

public class Request {
    private static ArrayList<User> waiting;

    public Request() {
        this.waiting = new ArrayList<>();
    }

    public static void newRequest(User user) {
        waiting.add(user);
    }

    public Match match(User user1, User user2) {
        Match game = new Match(7, 6, Mode.MultiPlayer, user1, user2);
        ArrayList<User> remove = new ArrayList<>();
        for (User u : waiting) {
            if (u.equals(user1) || u.equals(user2))
                remove.add(u);
        }
        waiting.removeAll(remove);
        return game;
    }
}