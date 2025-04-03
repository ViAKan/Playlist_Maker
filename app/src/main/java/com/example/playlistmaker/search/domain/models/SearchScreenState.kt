package com.example.playlistmaker.search.domain.models

data class SearchScreenState(
    val trackList: List<Track> = emptyList(),
    val error: Boolean = false,
    val empty: Boolean = false
)