package com.example.playlistmaker.search.domain.historyInteractor

import com.example.playlistmaker.search.domain.models.Track

interface HistoryInteractor {
    fun addInHistory(track: Track)
    fun clearHistory()
    fun getHistoryFromSph(): ArrayList<Track>
}