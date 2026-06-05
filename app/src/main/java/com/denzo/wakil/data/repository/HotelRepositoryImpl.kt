package com.denzo.wakil.data.repository

import android.content.Context
import com.denzo.wakil.Database.*
import com.denzo.wakil.Util.Hotel
import com.denzo.wakil.Util.Reader
import com.denzo.wakil.domain.repository.HotelRepository
import java.util.concurrent.Executors
import javax.inject.Inject

class HotelRepositoryImpl @Inject constructor(
    private val context: Context,
    private val bookingDao: BookingDao,
    private val housePostDao: HousePostDao,
    private val draftDao: DraftDao,
    private val blockedDao: BlockedDao
) : HotelRepository {

    private val executor = Executors.newFixedThreadPool(4)

    override fun getStaticHotels(): List<Hotel> {
        return Reader.getRestaurantList(context)
    }

    override fun getUserPosts(): List<HousePostEntity> {
        return housePostDao.getAllPosts()
    }

    override fun getBookings(username: String): List<BookingEntity> {
        return bookingDao.getBookingsByUser(username)
    }

    override fun insertBooking(booking: BookingEntity, onComplete: (() -> Unit)?) {
        executor.execute {
            bookingDao.insertBooking(booking)
            onComplete?.invoke()
        }
    }

    override fun deleteBooking(booking: BookingEntity, onComplete: (() -> Unit)?) {
        executor.execute {
            bookingDao.deleteBooking(booking)
            onComplete?.invoke()
        }
    }

    override fun clearAllBookings(username: String, onComplete: (() -> Unit)?) {
        executor.execute {
            bookingDao.clearAllBookings(username)
            onComplete?.invoke()
        }
    }

    override fun getBlockedUsers(username: String): List<String> {
        return blockedDao.getBlockedUsers(username)
    }

    override fun getBlockedPosts(username: String): List<Integer> {
        return blockedDao.getBlockedPosts(username)
    }
}
