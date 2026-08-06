package com.denzo.wakil.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denzo.wakil.Database.BookingEntity
import com.denzo.wakil.Database.HousePostEntity
import com.denzo.wakil.Util.Hotel
import com.denzo.wakil.Util.HotelView
import com.denzo.wakil.domain.repository.HotelRepository
import com.denzo.wakil.R
import java.util.ArrayList
import java.util.Random
import java.util.stream.Collectors
import javax.inject.Inject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val repository: HotelRepository
) : AndroidViewModel(application) {

    private val _hotels = MutableLiveData<List<HotelView>>()
    val hotels: LiveData<List<HotelView>> = _hotels
    
    private var allHotelsList: List<HotelView> = emptyList()

    fun loadHotels(username: String?) {
        Thread {
            val cover = intArrayOf(R.drawable.hicon1, R.drawable.hicon2, R.drawable.hicon3, R.drawable.hicon4)
            val random = Random()
            val bookings = username?.let { repository.getBookings(it) } ?: emptyList()
            val blockedUsers = username?.let { repository.getBlockedUsers(it) } ?: emptyList()
            val blockedPosts = username?.let { repository.getBlockedPosts(it) } ?: emptyList()

            val staticHotels = repository.getStaticHotels()
            val userPosts = repository.getUserPosts()

            val hotelList = ArrayList<HotelView>()

            // Add static hotels
            if (staticHotels != null) {
                val bookedIds = bookings.map { it.hotelId }

                for (h in staticHotels) {
                    if (!bookedIds.contains(h.id) && !blockedPosts.contains(h.id)) {
                        val idx = random.nextInt(4)
                        hotelList.add(HotelView(h.id, h.name, h.location, cover[idx], h.rating, h.feats, h.contact, "admin", h.pricePerNight))
                    }
                }
            }

            // Add user posts
            for (p in userPosts) {
                if (!blockedUsers.contains(p.username) && !blockedPosts.contains(p.id)) {
                    hotelList.add(HotelView(p.id, p.title, p.location, p.thumbnail, p.rating, p.features, p.contact, p.username, 100.0))
                }
            }

            allHotelsList = ArrayList(hotelList)
            _hotels.postValue(hotelList)
        }.start()
    }

    fun filterHotelsByPrice(min: Float, max: Float) {
        if (allHotelsList.isEmpty()) return
        val filtered = allHotelsList.stream()
                .filter { h -> h.pricePerNight >= min && h.pricePerNight <= max }
                .collect(Collectors.toList())
        _hotels.postValue(filtered)
    }

    fun filterHotels(type: String) {
        if (allHotelsList.isEmpty()) return

        val filtered = when (type) {
            "High Rating" -> allHotelsList.stream().filter { h -> h.rating >= 5 }.collect(Collectors.toList())
            "Budget" -> allHotelsList.stream().filter { h -> h.pricePerNight <= 100 }.collect(Collectors.toList())
            "Luxury" -> allHotelsList.stream().filter { h -> h.pricePerNight > 100 }.collect(Collectors.toList())
            else -> ArrayList(allHotelsList)
        }
        _hotels.postValue(filtered)
    }

    fun loadMyBookings(username: String?) {
        Thread {
            val bookings = username?.let { repository.getBookings(it) } ?: emptyList()
            val allHotels = repository.getStaticHotels()
            val bookedList = ArrayList<HotelView>()
            val cover = intArrayOf(R.drawable.hicon1, R.drawable.hicon2, R.drawable.hicon3, R.drawable.hicon4)
            val random = Random()

            if (allHotels != null) {
                for (be in bookings) {
                    for (h in allHotels) {
                        if (be.hotelId == h.id) {
                            val idx = random.nextInt(4)
                            bookedList.add(HotelView(
                                    h.id, 
                                    h.name, 
                                    h.location, 
                                    cover[idx], 
                                    h.rating, 
                                    h.feats, 
                                    h.contact, 
                                    "admin", 
                                    be.checkInDate, 
                                    be.checkOutDate, 
                                    be.guestsCount, 
                                    be.totalPrice, 
                                    be.status
                            ))
                            break
                        }
                    }
                }
            }
            _hotels.postValue(bookedList)
        }.start()
    }
}
