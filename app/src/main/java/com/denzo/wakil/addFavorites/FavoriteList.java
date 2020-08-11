package com.denzo.wakil.addFavorites;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName="favoritelist")

public class FavoriteList {
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "prname")
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
