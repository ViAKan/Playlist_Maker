package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface TrackRepository {
    fun search(text: String): List<Track>?
}