package org.example.objects;

public class MovieVersion {
    private final int id;
    private final int movieId;
    private final String movieFormat;
    private final String movieResolution;
    private final String movieLink;

    public MovieVersion(int id, int movieId, String movieFormat, String movieResolution, String movieLink) {
        this.id = id;
        this.movieId = movieId;
        this.movieFormat = movieFormat;
        this.movieResolution = movieResolution;
        this.movieLink = movieLink;
    }

    public int getId() {
        return id;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getMovieFormat() {
        return movieFormat;
    }

    public String getMovieResolution() {
        return movieResolution;
    }

    public String getMovieLink() {
        return movieLink;
    }
}
