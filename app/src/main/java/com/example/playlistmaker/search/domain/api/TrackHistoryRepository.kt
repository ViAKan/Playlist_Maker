package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.Track

interface TrackHistoryRepository {
    fun getHistoryFromSph(): ArrayList<Track>
    fun clearHistory()
    fun addInHistory(track: Track)
}