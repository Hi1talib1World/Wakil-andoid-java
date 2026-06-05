package com.denzo.wakil.domain.repository

import com.denzo.wakil.Database.BookingEntity
import com.denzo.wakil.Database.HousePostEntity
import com.denzo.wakil.Util.Hotel

interface HotelRepository {
    fun getStaticHotels(): List<Hotel>
    fun getUserPosts(): List<HousePostEntity>
    fun getBookings(username: String): List<BookingEntity>
    fun insertBooking(booking: BookingEntity, onComplete: (() -> Unit)?)
    fun deleteBooking(booking: BookingEntity, onComplete: (() -> Unit)?)
    fun clearAllBookings(username: String, onComplete: (() -> Unit)?)
    fun getBlockedUsers(username: String): List<String>
    fun getBlockedPosts(username: String): List<Int>
}
