package com.example.playlistmaker.media.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "track_table")
data class TrackEntity (
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
    val previewUrl: String
)