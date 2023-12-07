package org.example.repositories;

import org.example.objects.ConnectDB;
import org.example.objects.MovieVersion;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieVersionRepository {
    private static MovieVersionRepository instance;
    private MovieVersionRepository() {}

    public static MovieVersionRepository getInstance() {
        if (instance == null) {
            instance = new MovieVersionRepository();
        }

        return instance;
    }

    public void insertMovieVersion(MovieVersion movieVersion) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement insertedMovieVersion = conn.prepareStatement("INSERT INTO movie_versions " +
                    "(movie_id, movie_format, movie_resolution, movie_link) VALUES (?, ?, ?, ?)")) {
                insertedMovieVersion.setInt(1, movieVersion.getMovieId());
                insertedMovieVersion.setString(2, movieVersion.getMovieFormat());
                insertedMovieVersion.setString(3, movieVersion.getMovieResolution());
                insertedMovieVersion.setString(4, movieVersion.getMovieLink());
                insertedMovieVersion.executeUpdate();
            }
        }
    }

    public MovieVersion getMovieVersion(int id) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement movieVersion = conn.prepareStatement("SELECT * FROM movie_versions WHERE version_id = ?")) {
                movieVersion.setInt(1, id);
                try (ResultSet rs = movieVersion.executeQuery()) {
                    rs.first();
                    return new MovieVersion(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5));
                }
            }
        }
    }

    public boolean deleteMovieVersion(int id) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement deletedMovieVersion = conn.prepareStatement("DELETE FROM movie_versions WHERE version_id = ?")) {
                String movieVersionPath = getMovieVersion(id).getMovieLink();
                File versionToDelete = new File(getFolderPath(movieVersionPath));
                if (!deleteDirectory(versionToDelete)) {
                    return false;
                }
                deletedMovieVersion.setInt(1, id);
                deletedMovieVersion.executeUpdate();
                return true;
            }
        }
    }

    public List<MovieVersion> getMovieVersions(int id) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement movieVersion = conn.prepareStatement("SELECT * FROM movie_versions WHERE movie_id = ?")) {
                movieVersion.setInt(1, id);
                try (ResultSet rs = movieVersion.executeQuery()) {
                    List<MovieVersion> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(new MovieVersion(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5)));
                    }
                    return list;
                }
            }
        }
    }

    public List<MovieVersion> getAllVersionsOfMovies() throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement movieVersions = conn.prepareStatement("SELECT * FROM movie_versions")) {
                try (ResultSet rs = movieVersions.executeQuery()) {
                    List<MovieVersion> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(new MovieVersion(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getString(4), rs.getString(5)));
                    }
                    return list;
                }
            }
        }
    }

    public boolean movieVersionExists(int id) throws SQLException {
        try (Connection conn = ConnectDB.getConnection()) {
            try (PreparedStatement movieVersion = conn.prepareStatement("SELECT * FROM movie_versions WHERE version_id = ?")) {
                movieVersion.setInt(1, id);
                try (ResultSet rs = movieVersion.executeQuery()) {
                    return rs.first();
                }
            }
        }
    }

    private String getFolderPath(String path) {
        int lastIndexOf = path.lastIndexOf("/");
        int slashCount = 0;
        int startIndex = 0;
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == ('/')) {
                slashCount++;
                if (slashCount == 3) {
                    startIndex = i;
                    break;
                }
            }
        }

        return "/home/miguel/movieFiles" + path.substring(startIndex, lastIndexOf);
    }

    private boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}
