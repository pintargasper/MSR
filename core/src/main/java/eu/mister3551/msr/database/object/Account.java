package eu.mister3551.msr.database.object;

public class Account {

    private final String username;
    private final String token;

    public Account(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
