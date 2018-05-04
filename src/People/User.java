package src.People;

public class User {
    String nickname, email, password;

    public User(String nickname, String email, String password, String confirm) {
        if (password.equals(confirm)) {
            this.nickname = nickname;
            this.email = email;
            this.password = password;
        } else
            throw new IllegalArgumentException("Password not correct");
    }
}