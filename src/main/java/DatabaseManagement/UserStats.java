package DatabaseManagement;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "stats")
public class UserStats {

    public static final String USER_ID_FIELD_NAME = "userId";
    public static final String PARTITE_FIELD_NAME = "partite";
    public static final String VITTORIE_FIELD_NAME = "vittorie";
    public static final String SCONFITTE_FIELD_NAME = "sconfitte";
    public static final String PUNTI_FIELD_NAME = "punti";

    @DatabaseField(canBeNull = false, foreign = true)
    private int userId;
    @DatabaseField
    private int partite;
    @DatabaseField
    private int vittorie;
    @DatabaseField
    private int pareggi;
    @DatabaseField
    private int sconfitte;
    @DatabaseField
    private int punti;

    public UserStats(){}

    public UserStats(int userId) {

        this.partite = 0;
        this.vittorie = 0;
        this.pareggi = 0;
        this.sconfitte = 0;
        this.punti = 0;
    }

    public int getPartite() {
        return partite;
    }

    public void setPartite(int partite) {
        this.partite = partite;
    }

    public int getVittorie() {
        return vittorie;
    }

    public void setVittorie(int vittorie) {
        this.vittorie = vittorie;
    }

    public int getPareggi() {
        return pareggi;
    }

    public void setPareggi(int pareggi) {
        this.pareggi = pareggi;
    }

    public int getSconfitte() {
        return sconfitte;
    }

    public void setSconfitte(int sconfitte) {
        this.sconfitte = sconfitte;
    }

    public int getPunti() {
        return punti;
    }

    public void setPunti(int punti) {
        this.punti = punti;
    }
}
