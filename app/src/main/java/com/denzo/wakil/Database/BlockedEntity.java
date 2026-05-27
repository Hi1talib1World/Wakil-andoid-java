package com.denzo.wakil.Database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "blocked")
public class BlockedEntity {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username; // Current user
    private String blockedUsername; // User to block (optional)
    private int blockedPostId; // Post ID to block (optional)

    public BlockedEntity(String username, String blockedUsername, int blockedPostId) {
        this.username = username;
        this.blockedUsername = blockedUsername;
        this.blockedPostId = blockedPostId;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getBlockedUsername() { return blockedUsername; }
    public void setBlockedUsername(String blockedUsername) { this.blockedUsername = blockedUsername; }
    public int getBlockedPostId() { return blockedPostId; }
    public void setBlockedPostId(int blockedPostId) { this.blockedPostId = blockedPostId; }
}
