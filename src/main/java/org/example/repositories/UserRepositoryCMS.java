package org.example.repositories;

import org.example.objects.ConnectDB;
import org.example.objects.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepositoryCMS {
    private static UserRepositoryCMS instance;
    private UserRepositoryCMS() {}

    public static UserRepositoryCMS getInstance() {
        if (instance == null) {
            instance = new UserRepositoryCMS();
        }

        return instance;
    }

    public void insertUser(User user) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement insertedUser = conn.prepareStatement("INSERT INTO users_cms (username, password) VALUES (?, ?);")) {
                insertedUser.setString(1, user.getUsername());
                insertedUser.setString(2, String.valueOf(user.getPassword().hashCode()));
                insertedUser.executeUpdate();
            }
        }
    }

    public void deleteUser(int id) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement deletedUser = conn.prepareStatement("DELETE FROM users_cms WHERE user_id = ?")) {
                deletedUser.setInt(1, id);
                deletedUser.executeUpdate();
            }
        }
    }

    public User getUser(String username) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement user = conn.prepareStatement("SELECT * FROM users_cms WHERE username = ?")) {
                user.setString(1, username);
                try (ResultSet rs = user.executeQuery()) {
                    rs.first();
                    return new User(rs.getInt(1), rs.getString(2), rs.getString(3));
                }
            }
        }
    }

    public List<User> getAllUsers() throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement users = conn.prepareStatement("SELECT * from users_cms")) {
                try (ResultSet rs = users.executeQuery()) {
                    List<User> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3)));
                    }
                    return list;
                }
            }
        }
    }

    public boolean userExists(String username) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement user = conn.prepareStatement("SELECT * FROM users_cms WHERE username = ?")) {
                user.setString(1, username);
                try (ResultSet rs = user.executeQuery()) {
                    return rs.first();
                }
            }
        }
    }

    public boolean userExistsById(int id) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement user = conn.prepareStatement("SELECT * FROM users_cms WHERE user_id = ?")) {
                user.setInt(1, id);
                try (ResultSet rs = user.executeQuery()) {
                    return rs.first();
                }
            }
        }
    }

    public boolean login(String username, String password) throws SQLException {
        if (!userExists(username)) {
            return false;
        }

        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement user = conn.prepareStatement("SELECT password FROM users_cms WHERE username = ?")) {
                user.setString(1, username);
                try (ResultSet rs = user.executeQuery()) {
                    rs.first();
                    return Objects.equals(rs.getString(1), String.valueOf(password.hashCode()));
                }
            }
        }
    }
}
