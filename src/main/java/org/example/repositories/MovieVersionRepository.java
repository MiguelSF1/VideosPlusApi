package org.example.repositories;

import org.example.objects.MovieVersion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieVersionRepository {
    private final Connection conn = DriverManager.getConnection("jdbc:mariadb://localhost/videosplus", "root", "ola123");

    public MovieVersionRepository() throws SQLException {}

    public void insertMovieVersion(MovieVersion movieVersion) throws SQLException {
        PreparedStatement insertedMovieVersion = conn.prepareStatement("INSERT INTO movie_versions (movie_id, movie_format, movie_resolution, movie_link) VALUES (?, ?, ?, ?)");
        insertedMovieVersion.setInt(1, movieVersion.getMovieId());
        insertedMovieVersion.setString(2, movieVersion.getMovieFormat());
        insertedMovieVersion.setString(3, movieVersion.getMovieResolution());
        insertedMovieVersion.setString(4, movieVersion.getMovieLink());
        insertedMovieVersion.executeUpdate();
    }

    public MovieVersion getMovieVersion(int id) throws SQLException {
        PreparedStatement movieVersion = conn.prepareStatement("SELECT * FROM movie_versions WHERE version_id = ?");
        movieVersion.setInt(1, id);
        ResultSet rs = movieVersion.executeQuery();
        rs.first();
        return new MovieVersion(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5));
    }

    public void deleteMovieVersion(int id) throws SQLException {
        PreparedStatement deletedMovieVersion = conn.prepareStatement("DELETE FROM movie_versions WHERE version_id = ?");
        deletedMovieVersion.setInt(1, id);
        deletedMovieVersion.executeUpdate();
    }

    public List<MovieVersion> getMovieVersions(int id) throws SQLException {
        PreparedStatement movieVersion = conn.prepareStatement("SELECT * FROM movie_versions WHERE movie_id = ?");
        movieVersion.setInt(1, id);
        ResultSet rs = movieVersion.executeQuery();
        rs.first();
        List<MovieVersion> list = new ArrayList<>();
        list.add(new MovieVersion(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5)));
        while (rs.next()) {
            list.add(new MovieVersion(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5)));
        }
        return list;
    }

    public List<MovieVersion> getAllVersionsOfMovies() throws SQLException {
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
        PreparedStatement movieVersion = conn.prepareStatement("SELECT * FROM movie_versions WHERE version_id = ?");
        movieVersion.setInt(1, id);
        ResultSet rs = movieVersion.executeQuery();
        return rs.first();
    }
}
