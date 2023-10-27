package org.example.repositories;


import org.example.objects.Movie;
import org.example.objects.MovieVersion;

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

    public void insertMovieVersion(MovieVersion movieVersion) throws SQLException {
        PreparedStatement insertedMovieVersion = conn.prepareStatement("INSERT INTO movie_versions (movie_id, movie_format, movie_resolution, movie_link) VALUES (?, ?, ?, ?)");
        insertedMovieVersion.setInt(1, movieVersion.getId());
        insertedMovieVersion.setString(2, movieVersion.getMovieFormat());
        insertedMovieVersion.setString(3, movieVersion.getMovieResolution());
        insertedMovieVersion.setString(4, movieVersion.getMovieLink());
        insertedMovieVersion.executeUpdate();
    }

    public void updateMovieVersion(MovieVersion movieVersion) throws SQLException {
        PreparedStatement updatedMovieVersion = conn.prepareStatement("UPDATE movie_versions SET movie_format = ?, movie_resolution = ?, movie_link = ? WHERE movie_id = ?");
        updatedMovieVersion.setString(1, movieVersion.getMovieFormat());
        updatedMovieVersion.setString(2, movieVersion.getMovieResolution());
        updatedMovieVersion.setString(3, movieVersion.getMovieLink());
        updatedMovieVersion.setInt(4, movieVersion.getMovieId());
        updatedMovieVersion.executeUpdate();
    }

    public void deleteMovieVersion(int id) throws SQLException {
        PreparedStatement deletedMovieVersion = conn.prepareStatement("DELETE FROM movie_versions WHERE movie_id = ?");
        deletedMovieVersion.setInt(1, id);
        deletedMovieVersion.executeUpdate();
    }

    // make it by format or resolution and movie id
    public MovieVersion getMovieVersion(int id) throws SQLException {
        PreparedStatement movieVersion = conn.prepareStatement("SELECT * FROM movie_versions WHERE movie_id = ?");
        movieVersion.setInt(1, id);
        ResultSet rs = movieVersion.executeQuery();
        rs.first();
        return new MovieVersion(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5));
    }

    public List<MovieVersion> getAllVersionsOfMovie() throws SQLException {
        PreparedStatement movieVersions = conn.prepareStatement("SELECT * FROM movie_versions");
        ResultSet rs = movieVersions.executeQuery();
        rs.first();
        List<MovieVersion> list = new ArrayList<>();
        list.add(new MovieVersion(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5)));
        while (rs.next()) {
            list.add(new MovieVersion(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5)));
        }
        return list;
    }

    public boolean movieVersionExists(int id) throws SQLException {
        PreparedStatement movieVersion = conn.prepareStatement("SELECT * FROM movie_versions WHERE movie_id = ?");
        movieVersion.setInt(1, id);
        ResultSet rs = movieVersion.executeQuery();
        return rs.first();
    }
}
