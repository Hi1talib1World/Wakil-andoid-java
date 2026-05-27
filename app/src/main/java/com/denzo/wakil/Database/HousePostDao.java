package com.denzo.wakil.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HousePostDao {
    @Insert
    void insert(HousePostEntity post);

    @Query("SELECT * FROM house_posts")
    List<HousePostEntity> getAllPosts();

    @Query("DELETE FROM house_posts WHERE id = :postId")
    void deletePost(int postId);
}
