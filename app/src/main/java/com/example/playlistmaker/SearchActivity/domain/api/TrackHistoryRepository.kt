package com.example.playlistmaker.SearchActivity.domain.api

import com.example.playlistmaker.SearchActivity.data.dto.TrackDto
import com.example.playlistmaker.SearchActivity.domain.models.Track
import com.example.playlistmaker.SearchActivity.ui.TrackAdapter

interface TrackHistoryRepository {
    fun getHistoryFromSph(): ArrayList<Track>
    fun clearHistory()
    fun addInHistory(track: Track, historyList: ArrayList<Track>)
}