package com.denzo.wakil.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BookingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertBooking(BookingEntity booking);

    @Query("SELECT * FROM bookings WHERE username = :username")
    List<BookingEntity> getBookingsByUser(String username);

    @Query("SELECT * FROM bookings WHERE username = :username AND hotelId = :hotelId")
    BookingEntity getSpecificBooking(String username, int hotelId);

    @Delete
    void deleteBooking(BookingEntity booking);
}
