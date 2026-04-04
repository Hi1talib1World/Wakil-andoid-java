package com.denzo.wakil.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "drafts")
public class DraftEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private int hotelId;

    public DraftEntity(String username, int hotelId) {
        this.username = username;
        this.hotelId = hotelId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public int getHotelId() { return hotelId; }
}
