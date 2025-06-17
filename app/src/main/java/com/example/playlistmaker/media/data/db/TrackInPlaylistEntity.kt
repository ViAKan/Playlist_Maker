package com.example.playlistmaker.media.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_playlists")
data class TrackInPlaylistEntity (
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val collectionName: String,
    @PrimaryKey
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val previewUrl: String,
    val timestamp: Long = System.currentTimeMillis()
)