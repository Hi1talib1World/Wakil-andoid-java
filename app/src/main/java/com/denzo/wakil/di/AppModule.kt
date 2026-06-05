package com.denzo.wakil.di

import android.content.Context
import com.denzo.wakil.Database.*
import com.denzo.wakil.data.repository.HotelRepositoryImpl
import com.denzo.wakil.domain.repository.HotelRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideBookingDao(db: AppDatabase): BookingDao = db.bookingDao()

    @Provides
    fun provideHousePostDao(db: AppDatabase): HousePostDao = db.housePostDao()

    @Provides
    fun provideDraftDao(db: AppDatabase): DraftDao = db.draftDao()

    @Provides
    fun provideBlockedDao(db: AppDatabase): BlockedDao = db.blockedDao()

    @Provides
    fun provideUserDao(db: AppDatabase): UserDao = db.userDao()

    @Provides
    @Singleton
    fun provideHotelRepository(
        @ApplicationContext context: Context,
        bookingDao: BookingDao,
        housePostDao: HousePostDao,
        draftDao: DraftDao,
        blockedDao: BlockedDao
    ): HotelRepository {
        return HotelRepositoryImpl(context, bookingDao, housePostDao, draftDao, blockedDao)
    }
}
