package org.example.objects;

public class User {
    private int id;
    private final String username;
    private String password;
    private int permissionLevel;

    public User(int id, String username, String password, int permissionLevel) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.permissionLevel = permissionLevel;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPermissionLevel(int permissionLevel) { this.permissionLevel = permissionLevel; }
}
