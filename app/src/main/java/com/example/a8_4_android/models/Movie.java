package com.example.a8_4_android.models;

public class Movie {
    private String id;
    private String title;
    private String genre;
    private String posterUrl;
    private String description;
    private String releaseDate;
    private String duration;
    private String ageRating;

    public Movie() {}

    public Movie(String id, String title, String genre, String posterUrl, String description, String releaseDate, String duration, String ageRating) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.posterUrl = posterUrl;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.ageRating = ageRating;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }
    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }
    public String getAgeRating() { return ageRating; }
    public void setAgeRating(String ageRating) { this.ageRating = ageRating; }
}