package com.denzo.wakil.domain.usecase

import com.denzo.wakil.Database.BookingEntity
import com.denzo.wakil.domain.repository.HotelRepository
import javax.inject.Inject

class GetBookingsUseCase @Inject constructor(
    private val repository: HotelRepository
) {
    operator fun invoke(username: String): List<BookingEntity> {
        return repository.getBookings(username)
    }
}
