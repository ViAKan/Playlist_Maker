package com.example.playlistmaker.media.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val description: String,
    val coverPath: String?,
    val tracksJson: String,
    val tracksCount: Int,
    val creationDate: Long,
    val totalDuration: Long
)