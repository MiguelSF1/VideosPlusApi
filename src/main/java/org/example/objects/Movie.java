package org.example.objects;

import java.sql.Date;

public class Movie {
    private int id;
    private String title;
    private int releaseDate;
    private int duration;
    private String poster;
    private float rating;
    private String summary;


    public Movie(int id, String title, int releaseDate, int duration, String poster, float rating, String summary) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.poster = poster;
        this.rating = rating;
        this.summary = summary;
    }

    public Movie() {

    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getReleaseDate() { return releaseDate; }

    public int getDuration() { return duration; }

    public String getPoster() { return poster; }

    public float getRating() { return rating; }


    public String getSummary() {
        return summary;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReleaseDate(int releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
}
