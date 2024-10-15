package com.example.playlistmaker.SearchActivity.domain.api

import com.example.playlistmaker.SearchActivity.domain.models.Track

interface TrackRepository {
    fun search(text: String): List<Track>?
}