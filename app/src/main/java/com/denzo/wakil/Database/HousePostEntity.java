package com.denzo.wakil.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "house_posts")
public class HousePostEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String title;
    private String location;
    private String contact;
    private String features;
    private int rating;
    private int thumbnail; // For simplicity, using resource ID like the original app

    public HousePostEntity(String username, String title, String location, String contact, String features, int rating, int thumbnail) {
        this.username = username;
        this.title = title;
        this.location = location;
        this.contact = contact;
        this.features = features;
        this.rating = rating;
        this.thumbnail = thumbnail;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }
    public String getFeatures() { return features; }
    public void setFeatures(String features) { this.features = features; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public int getThumbnail() { return thumbnail; }
    public void setThumbnail(int thumbnail) { this.thumbnail = thumbnail; }
}
