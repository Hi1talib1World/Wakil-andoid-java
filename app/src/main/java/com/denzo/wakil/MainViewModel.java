package com.denzo.wakil;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.denzo.wakil.Database.BookingEntity;
import com.denzo.wakil.Database.HotelRepository;
import com.denzo.wakil.Database.HousePostEntity;
import com.denzo.wakil.Util.Hotel;
import com.denzo.wakil.Util.HotelView;
import com.denzo.wakil.Util.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainViewModel extends AndroidViewModel {
    private final HotelRepository repository;
    private final MutableLiveData<List<HotelView>> _hotels = new MutableLiveData<>();
    public final LiveData<List<HotelView>> hotels = _hotels;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new HotelRepository(application);
    }

    public void loadHotels(String username) {
        new Thread(() -> {
            int[] cover = {R.drawable.hicon1, R.drawable.hicon2, R.drawable.hicon3, R.drawable.hicon4};
            Random random = new Random();
            List<BookingEntity> bookings = repository.getBookings(username);
            List<String> blockedUsers = repository.getBlockedUsers(username);
            List<Integer> blockedPosts = repository.getBlockedPosts(username);

            List<Hotel> staticHotels = repository.getStaticHotels();
            List<HousePostEntity> userPosts = repository.getUserPosts();

            List<HotelView> hotelList = new ArrayList<>();

            // Add static hotels
            if (staticHotels != null) {
                List<Integer> bookedIds = new ArrayList<>();
                for (BookingEntity be : bookings) bookedIds.add(be.getHotelId());

                for (Hotel h : staticHotels) {
                    if (!bookedIds.contains(h.getId()) && !blockedPosts.contains(h.getId())) {
                        int idx = random.nextInt(4);
                        hotelList.add(new HotelView(h.getId(), h.getName(), h.getLocation(), cover[idx], h.getRating(), h.getFeats(), h.getContact(), "admin"));
                    }
                }
            }

            // Add user posts
            for (HousePostEntity p : userPosts) {
                if (!blockedUsers.contains(p.getUsername()) && !blockedPosts.contains(p.getId())) {
                    hotelList.add(new HotelView(p.getId(), p.getTitle(), p.getLocation(), p.getThumbnail(), p.getRating(), p.getFeatures(), p.getContact(), p.getUsername()));
                }
            }

            _hotels.postValue(hotelList);
        }).start();
    }

    public void loadMyBookings(String username) {
        new Thread(() -> {
            List<BookingEntity> bookings = repository.getBookings(username);
            List<Hotel> allHotels = repository.getStaticHotels();
            List<HotelView> bookedList = new ArrayList<>();
            int[] cover = {R.drawable.hicon1, R.drawable.hicon2, R.drawable.hicon3, R.drawable.hicon4};
            Random random = new Random();

            if (allHotels != null) {
                for (BookingEntity be : bookings) {
                    for (Hotel h : allHotels) {
                        if (be.getHotelId() == h.getId()) {
                            int idx = random.nextInt(4);
                            bookedList.add(new HotelView(
                                    h.getId(), 
                                    h.getName(), 
                                    h.getLocation(), 
                                    cover[idx], 
                                    h.getRating(), 
                                    h.getFeats(), 
                                    h.getContact(), 
                                    "admin", 
                                    be.getCheckInDate(), 
                                    be.getCheckOutDate(), 
                                    be.getGuestsCount(), 
                                    be.getTotalPrice(), 
                                    be.getStatus()
                            ));
                            break;
                        }
                    }
                }
            }
            _hotels.postValue(bookedList);
        }).start();
    }
}
