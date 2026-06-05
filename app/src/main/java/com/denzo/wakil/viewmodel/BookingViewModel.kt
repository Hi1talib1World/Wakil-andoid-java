package com.denzo.wakil.viewmodel

import androidx.lifecycle.ViewModel
import com.denzo.wakil.Database.BookingEntity
import com.denzo.wakil.domain.usecase.CreateBookingUseCase
import com.denzo.wakil.domain.usecase.GetBookingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val createBookingUseCase: CreateBookingUseCase,
    private val getBookingsUseCase: GetBookingsUseCase
) : ViewModel() {

    private val _bookings = MutableStateFlow<List<BookingEntity>>(emptyList())
    val bookings: StateFlow<List<BookingEntity>> = _bookings

    fun loadBookings(username: String) {
        _bookings.value = getBookingsUseCase(username)
    }

    fun createBooking(booking: BookingEntity, onComplete: () -> Unit) {
        createBookingUseCase(booking, onComplete)
    }
}
