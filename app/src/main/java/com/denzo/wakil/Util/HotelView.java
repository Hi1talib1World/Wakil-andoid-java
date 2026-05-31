package com.denzo.wakil.Util;

public class HotelView {
    private int id; // Added to uniquely identify for blocking/sharing
    private String name;
    private String location;
    private int thumbnail;
    private int rating;
    private String features;
    private String contact; // Added for social contact
    private String ownerUsername; // Added to identify who posted it
    private String bookingDate;
    private int guestsCount;
    private boolean isBooked;

    public HotelView(){
    }

    public HotelView(String name, String location, int thumbnail, int rating, String features) {
        this.name = name;
        this.location = location;
        this.thumbnail = thumbnail;
        this.rating = rating;
        this.features = features;
    }

    public HotelView(int id, String name, String location, int thumbnail, int rating, String features, String contact, String ownerUsername) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.thumbnail = thumbnail;
        this.rating = rating;
        this.features = features;
        this.contact = contact;
        this.ownerUsername = ownerUsername;
    }

    public HotelView(int id, String name, String location, int thumbnail, int rating, String features, String contact, String ownerUsername, String bookingDate, int guestsCount) {
        this(id, name, location, thumbnail, rating, features, contact, ownerUsername);
        this.bookingDate = bookingDate;
        this.guestsCount = guestsCount;
        this.isBooked = true;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getRating() {
        return rating;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getFeatures() {
        return features;
    }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getOwnerUsername() { return ownerUsername; }
    public void setOwnerUsername(String ownerUsername) { this.ownerUsername = ownerUsername; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public int getGuestsCount() { return guestsCount; }
    public void setGuestsCount(int guestsCount) { this.guestsCount = guestsCount; }

    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }
}
