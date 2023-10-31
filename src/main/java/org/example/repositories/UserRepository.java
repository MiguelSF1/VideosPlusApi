package org.example.repositories;

import org.example.objects.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost/videosplus", "root", "ola123");

    public UserRepository() throws SQLException {}

    public void insertUser(User user) throws SQLException {
        PreparedStatement insertedUser = conn.prepareStatement("INSERT INTO users (username, password, permission_level) VALUES (?, ?, ?);");
        insertedUser.setString(1, user.getUsername());
        insertedUser.setString(2, user.getPassword());
        insertedUser.setInt(3, user.getPermissionLevel());
        insertedUser.executeUpdate();
    }

    public void updateUser(User user) throws SQLException {
        PreparedStatement updatedUser = conn.prepareStatement("UPDATE users SET password = ?, username = ?, permission_level = ? WHERE user_id = ?");
        updatedUser.setString(1, user.getPassword());
        updatedUser.setString(2, user.getUsername());
        updatedUser.setInt(3, user.getPermissionLevel());
        updatedUser.setInt(4, user.getId());
        updatedUser.executeUpdate();
    }

    public void deleteUser(int id) throws SQLException {
        PreparedStatement deletedUser = conn.prepareStatement("DELETE FROM users WHERE user_id = ?");
        deletedUser.setInt(1, id);
        deletedUser.executeUpdate();
    }


    public User getUser(int id) throws SQLException {
        PreparedStatement user = conn.prepareStatement("SELECT * FROM users WHERE user_id = ?");
        user.setInt(1, id);
        ResultSet rs = user.executeQuery();
        rs.first();
        return new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4));
    }


    public List<User> getAllUsers() throws SQLException {
        PreparedStatement users = conn.prepareStatement("SELECT * from users");
        ResultSet rs = users.executeQuery();
        rs.first();
        List<User> list = new ArrayList<>();
        list.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
        while (rs.next()) {
            list.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getInt(4)));
        }
        return list;
    }

    public boolean userExists(int id) throws SQLException {
        PreparedStatement user = conn.prepareStatement("SELECT * FROM users WHERE user_id = ?");
        user.setInt(1, id);
        ResultSet rs = user.executeQuery();
        return rs.first();
    }

}

