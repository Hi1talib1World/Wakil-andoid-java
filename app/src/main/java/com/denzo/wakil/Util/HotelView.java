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
    private String checkInDate;
    private String checkOutDate;
    private int guestsCount;
    private double totalPrice;
    private String status;
    private double pricePerNight;
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

    public HotelView(int id, String name, String location, int thumbnail, int rating, String features, String contact, String ownerUsername, double pricePerNight) {
        this(id, name, location, thumbnail, rating, features, contact, ownerUsername);
        this.pricePerNight = pricePerNight;
    }

    public HotelView(int id, String name, String location, int thumbnail, int rating, String features, String contact, String ownerUsername, String checkInDate, String checkOutDate, int guestsCount, double totalPrice, String status) {
        this(id, name, location, thumbnail, rating, features, contact, ownerUsername);
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.guestsCount = guestsCount;
        this.totalPrice = totalPrice;
        this.status = status;
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

    public String getCheckInDate() { return checkInDate; }
    public void setCheckInDate(String checkInDate) { this.checkInDate = checkInDate; }

    public String getCheckOutDate() { return checkOutDate; }
    public void setCheckOutDate(String checkOutDate) { this.checkOutDate = checkOutDate; }

    public int getGuestsCount() { return guestsCount; }
    public void setGuestsCount(int guestsCount) { this.guestsCount = guestsCount; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public double getPricePerNight() { return pricePerNight; }
    public void setPricePerNight(double pricePerNight) { this.pricePerNight = pricePerNight; }

    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean booked) { isBooked = booked; }
}
