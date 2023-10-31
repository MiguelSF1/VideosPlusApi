package org.example.objects;

import java.sql.Date;

public class Movie {
    private final int id;
    private final String title;
    private final Date releaseDate;
    private final int duration;
    private final String poster;
    private final float rating;
    private final String genre;
    private final String summary;


    public Movie(int id, String title, Date releaseDate, int duration, String poster, float rating, String genre , String summary) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.poster = poster;
        this.rating = rating;
        this.genre = genre;
        this.summary = summary;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Date getReleaseDate() { return releaseDate; }

    public int getDuration() { return duration; }

    public String getPoster() { return poster; }

    public float getRating() { return rating; }

    public String getGenre() { return genre; }

    public String getSummary() {
        return summary;
    }
}
