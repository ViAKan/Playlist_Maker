package com.example.playlistmaker.media.domain.model

data class Playlist (
    val id: Long = 0,
    val name: String,
    val description: String,
    val coverPath: String?,
    val tracksJson: String,
    val tracksCount: Int,
    val creationDate: Long,
    val totalDuration: Long
)