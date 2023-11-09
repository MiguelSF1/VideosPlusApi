package org.example.objects;


public class User {
    private final int id;
    private final String username;
    private final String password;
    private final int permissionLevel;

    public User(int id, String username, String password, int permissionLevel) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.permissionLevel = permissionLevel;
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public int getPermissionLevel() { return permissionLevel; }

}
