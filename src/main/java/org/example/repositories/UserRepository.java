package org.example.repositories;

import org.example.objects.Movie;
import org.example.objects.User;

import java.sql.*;
import java.util.List;

public class UserRepository {
    private final Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost/videosplus", "root", "ola123");
    private final Statement stmt = conn.createStatement();

    public UserRepository() throws SQLException {
    }

    public void insertUser(User user) {

    }

    public void updateUser(User user) {

    }

    public void deleteUser(int id) {

    }

    public User getUser(int id) {
        return null;
    }

    public List<User> getAllUsers() throws SQLException {
        ResultSet rs = stmt.executeQuery("");
        return null;
    }

    public boolean userExists(int id) {
        return false;
    }
}
