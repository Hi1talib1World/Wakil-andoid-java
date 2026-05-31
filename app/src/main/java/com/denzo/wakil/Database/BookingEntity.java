package com.denzo.wakil.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "bookings")
public class BookingEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private int hotelId;
    private String bookingDate;
    private int guestsCount;
    private String specialRequests;

    public BookingEntity(String username, int hotelId, String bookingDate, int guestsCount, String specialRequests) {
        this.username = username;
        this.hotelId = hotelId;
        this.bookingDate = bookingDate;
        this.guestsCount = guestsCount;
        this.specialRequests = specialRequests;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public int getHotelId() { return hotelId; }
    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }
    public int getGuestsCount() { return guestsCount; }
    public void setGuestsCount(int guestsCount) { this.guestsCount = guestsCount; }
    public String getSpecialRequests() { return specialRequests; }
    public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
}
