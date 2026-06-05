package com.denzo.wakil.domain.usecase

import com.denzo.wakil.Database.BookingEntity
import com.denzo.wakil.domain.repository.HotelRepository
import javax.inject.Inject

class CreateBookingUseCase @Inject constructor(
    private val repository: HotelRepository
) {
    operator fun invoke(booking: BookingEntity, onComplete: (() -> Unit)? = null) {
        repository.insertBooking(booking, onComplete)
    }
}
