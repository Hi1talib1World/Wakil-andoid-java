package com.denzo.wakil.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface BlockedDao {
    @Insert
    void insert(BlockedEntity blocked);

    @Query("SELECT blockedUsername FROM blocked WHERE username = :username AND blockedUsername IS NOT NULL")
    List<String> getBlockedUsers(String username);

    @Query("SELECT blockedPostId FROM blocked WHERE username = :username AND blockedPostId != 0")
    List<Integer> getBlockedPosts(String username);
}
