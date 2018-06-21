package DatabaseManagement;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "user")
public class User {

    public static final String USERNAME_FIELD_NAME = "username";
    public static final String EMAIL_FIELD_NAME = "email";
    public static final String PASSWORD_FIELD_NAME = "password";
    public static final String AUTH_TOKEN_FIELD_NAME = "auth_token";
    public static final String EMAIL_CONFIRMED_FIELD_NAME = "email_confirmed";
    public static final String EMAIL_TOKEN_FIELD_NAME = "email_token";

    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField(unique = true, columnName = USERNAME_FIELD_NAME)
    private String username;
    @DatabaseField(unique = true, columnName = EMAIL_FIELD_NAME)
    private String email;
    @DatabaseField(canBeNull = false, columnName = PASSWORD_FIELD_NAME)
    private String password;
    @DatabaseField(columnName = AUTH_TOKEN_FIELD_NAME)
    private String token;
    @DatabaseField(columnName = EMAIL_CONFIRMED_FIELD_NAME)
    private boolean email_confirmed;
    @DatabaseField(columnName = EMAIL_TOKEN_FIELD_NAME)
    private String email_token;

    public User() {
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail_confirmed(boolean email_confirmed) {
        this.email_confirmed = email_confirmed;
    }

    public void setEmail_token(String email_token) {
        this.email_token = email_token;
    }

}
