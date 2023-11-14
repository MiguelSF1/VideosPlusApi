package org.example.repositories;

import org.example.objects.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserRepository {
    private final Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost/videosplus", "root", "ola123");

    public UserRepository() throws SQLException {}

    public User insertUser(User user) throws SQLException {
        PreparedStatement insertedUser = conn.prepareStatement("INSERT INTO users (username, password) VALUES (?, ?);");
        insertedUser.setString(1, user.getUsername());
        insertedUser.setString(2, user.getPassword());
        insertedUser.executeUpdate();
        return this.getUser(user.getUsername());
    }

    public User updateUser(User user) throws SQLException {
        PreparedStatement updatedUser = conn.prepareStatement("UPDATE users SET password = ?, username = ? WHERE user_id = ?");
        updatedUser.setString(1, user.getPassword());
        updatedUser.setString(2, user.getUsername());
        updatedUser.setInt(3, user.getId());
        updatedUser.executeUpdate();
        return this.getUser(user.getUsername());
    }

    public void deleteUser(int id) throws SQLException {
        PreparedStatement deletedUser = conn.prepareStatement("DELETE FROM users WHERE user_id = ?");
        deletedUser.setInt(1, id);
        deletedUser.executeUpdate();
    }


    public User getUser(String username) throws SQLException {
        PreparedStatement user = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
        user.setString(1, username);
        ResultSet rs = user.executeQuery();
        rs.first();
        return new User(rs.getInt(1), rs.getString(2), rs.getString(3));
    }


    public List<User> getAllUsers() throws SQLException {
        PreparedStatement users = conn.prepareStatement("SELECT * from users");
        ResultSet rs = users.executeQuery();
        rs.first();
        List<User> list = new ArrayList<>();
        list.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3)));
        while (rs.next()) {
            list.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }
        return list;
    }

    public boolean userExists(String username) throws SQLException {
        PreparedStatement user = conn.prepareStatement("SELECT * FROM users WHERE username = ?");
        user.setString(1, username);
        ResultSet rs = user.executeQuery();
        return rs.first();
    }

    public boolean login(String username, String password) throws SQLException {
        if (!userExists(username)) {
            return false;
        }

        PreparedStatement user = conn.prepareStatement("SELECT password FROM users WHERE username = ?");
        user.setString(1, username);
        ResultSet rs = user.executeQuery();
        rs.first();

        return Objects.equals(rs.getString(1), password);
    }
}

