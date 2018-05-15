package People;

public class Player {
    User user;
    int score, id; // id: 1 or 2

    public Player(User user, int id) {
        this.user=user;
        this.id = id;
        score =0;
    }

    public int getId() {
        return id;
    }

    public void setScore(int score) {
        this.score = score;
    }
}