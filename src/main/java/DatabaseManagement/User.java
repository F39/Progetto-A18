package DatabaseManagement;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

// TODO : user entity to be mapped to db
@DatabaseTable(tableName = "user")
public class User {

        @DatabaseField(generatedId = true)
        private Integer id;
        @DatabaseField(unique = true)
        private String user;
        @DatabaseField(unique = true)
        private String mail;
        @DatabaseField
        private String password;

        public User(){}


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public String getMail() {
            return mail;
        }

        public void setMail(String mail) {
            this.mail = mail;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
}
