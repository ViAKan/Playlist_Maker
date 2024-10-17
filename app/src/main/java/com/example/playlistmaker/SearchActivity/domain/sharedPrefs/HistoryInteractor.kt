package com.example.playlistmaker.SearchActivity.domain.sharedPrefs

import com.example.playlistmaker.SearchActivity.domain.models.Track

interface HistoryInteractor {
    fun addInHistory(track: Track)
    fun clearHistory()
    fun getHistoryFromSph(): ArrayList<Track>
}