package com.denzo.wakil.Database;

import android.content.Context;
import com.denzo.wakil.Util.Hotel;
import com.denzo.wakil.Util.Reader;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HotelRepository {
    private final BookingDao bookingDao;
    private final HousePostDao housePostDao;
    private final DraftDao draftDao;
    private final BlockedDao blockedDao;
    private final Context context;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);

    public HotelRepository(Context context) {
        AppDatabase db = AppDatabase.getInstance(context);
        this.bookingDao = db.bookingDao();
        this.housePostDao = db.housePostDao();
        this.draftDao = db.draftDao();
        this.blockedDao = db.blockedDao();
        this.context = context.getApplicationContext();
    }

    public List<Hotel> getStaticHotels() {
        return Reader.getRestaurantList(context);
    }

    public List<HousePostEntity> getUserPosts() {
        return housePostDao.getAllPosts();
    }

    public List<BookingEntity> getBookings(String username) {
        return bookingDao.getBookingsByUser(username);
    }

    public void insertBooking(BookingEntity booking, Runnable onComplete) {
        executor.execute(() -> {
            bookingDao.insertBooking(booking);
            if (onComplete != null) onComplete.run();
        });
    }

    public void deleteBooking(BookingEntity booking, Runnable onComplete) {
        executor.execute(() -> {
            bookingDao.deleteBooking(booking);
            if (onComplete != null) onComplete.run();
        });
    }

    public void clearAllBookings(String username, Runnable onComplete) {
        executor.execute(() -> {
            bookingDao.clearAllBookings(username);
            if (onComplete != null) onComplete.run();
        });
    }

    public List<String> getBlockedUsers(String username) {
        return blockedDao.getBlockedUsers(username);
    }

    public List<Integer> getBlockedPosts(String username) {
        return blockedDao.getBlockedPosts(username);
    }
}
