package org.example.repositories;

import org.example.objects.ConnectDB;
import org.example.objects.User;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private static UserRepository instance;

    private UserRepository() {}

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }

        return instance;
    }

    public void insertUser(User user) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement insertedUser = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?);")) {
                Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
                insertedUser.setString(1, user.getUsername());
                insertedUser.setString(2, pbkdf2PasswordEncoder.encode(user.getPassword()));
                insertedUser.executeUpdate();
            }
        }
    }

    public void deleteUser(int id) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement deletedUser = conn.prepareStatement("DELETE FROM users WHERE user_id = ?")) {
                deletedUser.setInt(1, id);
                deletedUser.executeUpdate();
            }
        }
    }

    public User getUser(String username) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement user = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
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
            try (PreparedStatement users = conn.prepareStatement("SELECT * from users")) {
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
            try (PreparedStatement user = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
                user.setString(1, username);
                try (ResultSet rs = user.executeQuery()) {
                    return rs.first();
                }
            }
        }
    }

    public boolean userExistsById(int id) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement user = conn.prepareStatement("SELECT * FROM users WHERE user_id = ?")) {
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
            try (PreparedStatement user = conn.prepareStatement("SELECT password FROM users WHERE username = ?")) {
                user.setString(1, username);
                try (ResultSet rs = user.executeQuery()) {
                    Pbkdf2PasswordEncoder pbkdf2PasswordEncoder = new Pbkdf2PasswordEncoder();
                    rs.first();
                    return pbkdf2PasswordEncoder.matches(password, rs.getString(1));
                }
            }
        }
    }
}

