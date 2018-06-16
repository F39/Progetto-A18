package DatabaseManagement;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "userlog")
public class UserLog {

    @DatabaseField(id = true)
    private String username;
    @DatabaseField
    private int partite ;
    @DatabaseField
    private int vittorie;
    @DatabaseField
    private int pareggi;
    @DatabaseField
    private int sconfitte;
    @DatabaseField
    private int punti;

    public UserLog() {
        this.partite = 0;
        this.vittorie = 0;
        this.pareggi = 0;
        this.sconfitte = 0;
        this.punti = 0;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
