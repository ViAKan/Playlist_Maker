package com.example.playlistmaker.media.data.dto

data class PlaylistDto (
    val id: Long = 0,
    val name: String,
    val description: String,
    val coverPath: String?,
    val tracksJson: String,
    val tracksCount: Int
)