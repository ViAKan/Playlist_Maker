package com.example.playlistmaker.search.data.dto

data class TrackDto (
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val collectionName: String,
    val trackId: Int,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val previewUrl: String
)