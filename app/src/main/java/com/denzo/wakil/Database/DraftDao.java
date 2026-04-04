package com.denzo.wakil.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DraftDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDraft(DraftEntity draft);

    @Query("SELECT * FROM drafts WHERE username = :username")
    List<DraftEntity> getDraftsByUser(String username);

    @Query("SELECT * FROM drafts WHERE username = :username AND hotelId = :hotelId")
    DraftEntity getSpecificDraft(String username, int hotelId);

    @Delete
    void deleteDraft(DraftEntity draft);
}
