package com.example.playlistmaker.search.data.sharedPreferences

import com.example.playlistmaker.search.data.dto.TrackDto

interface TrackManager {
    fun getHistoryFromSph(): ArrayList<TrackDto>
    fun clearHistory()
    fun addInHistory(track: TrackDto)
}