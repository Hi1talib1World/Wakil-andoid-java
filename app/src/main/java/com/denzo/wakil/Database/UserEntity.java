package com.denzo.wakil.Database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class UserEntity {
    @PrimaryKey
    @NonNull
    private String username;
    private String password;

    public UserEntity(@NonNull String username, String password) {
        this.username = username;
        this.password = password;
    }

    @NonNull
    public String getUsername() { return username; }
    public String getPassword() { return password; }
}
