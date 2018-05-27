package DatabaseManagement;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

// TODO : user entity to be mapped to db
@DatabaseTable(tableName = "user")
public class User {

        @DatabaseField(generatedId = true)
        private Integer id;
        @DatabaseField(unique = true)
        private String username;
        @DatabaseField(unique = true)
        private String email;
        @DatabaseField
        private String password;

        public User(){}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username, String email, String password){
            this.username = username;
            this.email=email;
            this.password=password;
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

        public void setUser(String user) {
            this.username = user;
        }

        public String getEmail() {
            return email;
        }

        public void setMail(String mail) {
            this.email = mail;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
}
