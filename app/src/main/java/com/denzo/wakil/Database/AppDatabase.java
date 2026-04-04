package com.denzo.wakil.Database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserEntity.class, BookingEntity.class, DraftEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract UserDao userDao();
    public abstract BookingDao bookingDao();
    public abstract DraftDao draftDao();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "wakil_database")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries() // Note: For a real app, use background threads/LiveData
                    .build();
        }
        return instance;
    }
}
