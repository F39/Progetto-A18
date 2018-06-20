package DatabaseManagement;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "stats")
public class UserStats {

    public static final String USER_ID_FIELD_NAME = "userId";
    public static final String GAMES_FIELD_NAME = "games";
    public static final String WINS_FIELD_NAME = "wins";
    public static final String DEFEATS_FIELD_NAME = "defeats";
    public static final String POINTS_FIELD_NAME = "points";
    public static final String TIES_FIELD_NAME = "ties";

    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField(canBeNull = false, foreign = true, columnName = USER_ID_FIELD_NAME)
    private User user;
    @DatabaseField(columnName = GAMES_FIELD_NAME)
    private int games;
    @DatabaseField(columnName = WINS_FIELD_NAME)
    private int wins;
    @DatabaseField(columnName = TIES_FIELD_NAME)
    private int ties;
    @DatabaseField(columnName = DEFEATS_FIELD_NAME)
    private int defeats;
    @DatabaseField(columnName = POINTS_FIELD_NAME)
    private int points;

    public UserStats(){}

    public UserStats(User user) {
        this.user = user;
        this.games = 0;
        this.wins = 0;
        this.ties = 0;
        this.defeats = 0;
        this.points = 0;
    }

    public User getUser() {
        return user;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() { return id; }

    public int getGames() {
        return games;
    }

    public void setGames(int games) {
        this.games = games;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getTies() {
        return ties;
    }

    public void setTies(int ties) {
        this.ties = ties;
    }

    public int getDefeats() {
        return defeats;
    }

    public void setDefeats(int defeats) {
        this.defeats = defeats;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
