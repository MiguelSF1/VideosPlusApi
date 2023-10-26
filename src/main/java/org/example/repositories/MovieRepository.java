package org.example.repositories;


import org.example.objects.Movie;
import org.example.objects.MovieVersion;

import java.sql.*;
import java.util.List;

public class MovieRepository {
    private final Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost/videosplus", "root", "ola123");
    private final Statement stmt = conn.createStatement();

    public MovieRepository() throws SQLException {
    }

    public void insertMovie(Movie movie) {

    }

    public void updateMovie(Movie movie) {

    }

    public void deleteMovie(int id) {

    }

    public Movie getMovie(int id) {
        return null;
    }

    public List<Movie> getAllMovies() throws SQLException {
        ResultSet rs = stmt.executeQuery("SELECT * from movies");
        return null;
    }

    public boolean movieExists(int id) {
        return false;
    }

    public void insertMovieVersion(MovieVersion movieVersion) {

    }

    public void updateMovieVersion(MovieVersion movieVersion) {

    }

    public void deleteMovieVersion(int id) {

    }

    public MovieVersion getMovieVersion(int id) {
        // by format or resolution
        return null;
    }

    public List<MovieVersion> getAllVersionsOfMovie() {
        return null;
    }

    public boolean movieVersionExists(int id) {
        return false;
    }
}
