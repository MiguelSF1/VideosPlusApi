package org.example.repositories;


import org.example.objects.ConnectDB;
import org.example.objects.Movie;
import org.example.objects.MovieVersion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieRepository {
    private static MovieRepository instance;

    private MovieRepository() {}

    public static MovieRepository getInstance() {
        if (instance == null) {
            instance = new MovieRepository();
        }

        return instance;
    }

    public void insertMovie(Movie movie) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement insertedMovie = conn.prepareStatement
                    ("INSERT INTO movies (title, release_date, duration, poster, rating, summary) VALUES (?, ?, ?, ?, ?, ?)")) {
                insertedMovie.setString(1, movie.getTitle());
                insertedMovie.setInt(2, movie.getReleaseDate());
                insertedMovie.setInt(3, movie.getDuration());
                insertedMovie.setString(4, movie.getPoster());
                insertedMovie.setFloat(5, movie.getRating());
                insertedMovie.setString(6, movie.getSummary());
                insertedMovie.executeUpdate();
            }
        }
    }

    public void updateMovie(Movie movie) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement updatedMovie = conn.prepareStatement
                    ("UPDATE movies SET title = ?, release_date = ?, duration = ?, poster = ?, rating = ?, summary = ? WHERE movie_id = ?")) {
                updatedMovie.setString(1, movie.getTitle());
                updatedMovie.setInt(2, movie.getReleaseDate());
                updatedMovie.setInt(3, movie.getDuration());
                updatedMovie.setString(4, movie.getPoster());
                updatedMovie.setFloat(5, movie.getRating());
                updatedMovie.setString(6, movie.getSummary());
                updatedMovie.setInt(7, movie.getId());
                updatedMovie.executeUpdate();
            }
        }
    }

    public void deleteMovie(int id) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement deletedMovie = conn.prepareStatement("DELETE FROM movies WHERE movie_id = ?")) {
                deletedMovie.setInt(1, id);
                deletedMovie.executeUpdate();
                List<MovieVersion> movieVersions = MovieVersionRepository.getInstance().getMovieVersions(id);
                for (MovieVersion movieVersion : movieVersions) {
                    MovieVersionRepository.getInstance().deleteMovieVersion(movieVersion.getId());
                }
            }
        }
    }

    public Movie getMovie(int id) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement movie = conn.prepareStatement("SELECT * FROM movies WHERE movie_id = ?")) {
                movie.setInt(1, id);
                try (ResultSet rs = movie.executeQuery()) {
                    rs.first();
                    return new Movie(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getFloat(6), rs.getString(7));
                }
            }
        }
    }

    public List<Movie> getAllMovies() throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement movies = conn.prepareStatement("SELECT * FROM movies")) {
                try (ResultSet rs = movies.executeQuery()) {
                    List<Movie> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(new Movie(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getString(5), rs.getFloat(6), rs.getString(7)));
                    }
                    return list;
                }
            }
        }
    }

    public boolean movieExists(int id) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement movie = conn.prepareStatement("SELECT * FROM movies WHERE movie_id = ?")) {
                movie.setInt(1, id);
                try (ResultSet rs = movie.executeQuery()) {
                    return rs.first();
                }
            }
        }
    }
}
