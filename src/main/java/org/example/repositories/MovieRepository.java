package org.example.repositories;


import org.example.objects.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {
    private final Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost/videosplus", "root", "ola123");

    public MovieRepository() throws SQLException {}

    public void insertMovie(Movie movie) throws SQLException {
        PreparedStatement insertedMovie = conn.prepareStatement("INSERT INTO movies (title, description) VALUES (?, ?)");
        insertedMovie.setString(1, movie.getTitle());
        insertedMovie.setString(2, movie.getDescription());
        insertedMovie.executeUpdate();
    }

    public void updateMovie(Movie movie) throws SQLException {
        PreparedStatement updatedMovie = conn.prepareStatement("UPDATE movies SET title = ?, description = ? WHERE movie_id = ?");
        updatedMovie.setString(1, movie.getTitle());
        updatedMovie.setString(2, movie.getDescription());
        updatedMovie.setInt(3, movie.getId());
        updatedMovie.executeUpdate();
    }

    public void deleteMovie(int id) throws SQLException {
        PreparedStatement deletedMovie = conn.prepareStatement("DELETE FROM movies WHERE movie_id = ?");
        deletedMovie.setInt(1, id);
        deletedMovie.executeUpdate();
    }

    public Movie getMovie(int id) throws SQLException {
        PreparedStatement movie = conn.prepareStatement("SELECT * FROM movies WHERE movie_id = ?");
        movie.setInt(1, id);
        ResultSet rs = movie.executeQuery();
        rs.first();
        return new Movie(rs.getInt(1), rs.getString(2), rs.getString(3));
    }

    public List<Movie> getAllMovies() throws SQLException {
        PreparedStatement movies = conn.prepareStatement("SELECT * FROM movies");
        ResultSet rs = movies.executeQuery();
        rs.first();
        List<Movie> list = new ArrayList<>();
        list.add(new Movie(rs.getInt(1), rs.getString(2), rs.getString(3)));
        while (rs.next()) {
            list.add(new Movie(rs.getInt(1), rs.getString(2), rs.getString(3)));
        }
        return list;
    }

    public boolean movieExists(int id) throws SQLException {
        PreparedStatement movie = conn.prepareStatement("SELECT * FROM movies WHERE movie_id = ?");
        movie.setInt(1, id);
        ResultSet rs = movie.executeQuery();
        return rs.first();
    }
}
