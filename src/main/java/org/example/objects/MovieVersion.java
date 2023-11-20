package org.example.objects;

public class MovieVersion {
    private int id;
    private int movieId;
    private String movieFormat;
    private String movieResolution;
    private String movieLink;

    public MovieVersion(int id, int movieId, String movieFormat, String movieResolution, String movieLink) {
        this.id = id;
        this.movieId = movieId;
        this.movieFormat = movieFormat;
        this.movieResolution = movieResolution;
        this.movieLink = movieLink;
    }

    public MovieVersion(int movieId, String movieFormat, String movieResolution, String movieLink) {
        this.movieId = movieId;
        this.movieFormat = movieFormat;
        this.movieResolution = movieResolution;
        this.movieLink = movieLink;
    }

    public MovieVersion() {

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

    public void setId(int id) {
        this.id = id;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public void setMovieFormat(String movieFormat) {
        this.movieFormat = movieFormat;
    }

    public void setMovieResolution(String movieResolution) {
        this.movieResolution = movieResolution;
    }

    public void setMovieLink(String movieLink) {
        this.movieLink = movieLink;
    }
}
