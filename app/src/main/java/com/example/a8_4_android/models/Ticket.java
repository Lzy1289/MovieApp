package com.example.a8_4_android.models;

import java.util.Date;

public class Ticket {
    private String id;
    private String userId;
    private String showtimeId;
    private String movieTitle;
    private String theaterName;
    private Date showtimeDate;
    private String seatNumbers;
    private double totalPrice;

    public Ticket() {}

    public Ticket(String id, String userId, String showtimeId, String movieTitle, String theaterName, Date showtimeDate, String seatNumbers, double totalPrice) {
        this.id = id;
        this.userId = userId;
        this.showtimeId = showtimeId;
        this.movieTitle = movieTitle;
        this.theaterName = theaterName;
        this.showtimeDate = showtimeDate;
        this.seatNumbers = seatNumbers;
        this.totalPrice = totalPrice;
    }

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getShowtimeId() { return showtimeId; }
    public String getMovieTitle() { return movieTitle; }
    public String getTheaterName() { return theaterName; }
    public Date getShowtimeDate() { return showtimeDate; }
    public String getSeatNumbers() { return seatNumbers; }
    public double getTotalPrice() { return totalPrice; }
}