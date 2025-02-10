package eu.mister3551.msr.database.object;

import lombok.Getter;

@Getter
public class Account {

    private final String username;
    private final String token;

    public Account(String username, String token) {
        this.username = username;
        this.token = token;
    }
}
