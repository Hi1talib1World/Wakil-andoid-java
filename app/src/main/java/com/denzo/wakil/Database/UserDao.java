package com.denzo.wakil.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUser(UserEntity user);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    UserEntity login(String username, String password);

    @Query("SELECT * FROM users WHERE username = :username")
    UserEntity getUserByUsername(String username);
}
