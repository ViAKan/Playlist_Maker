package com.example.playlistmaker.SearchActivity.data.sharedPreferences

import android.content.SharedPreferences
import com.example.playlistmaker.SearchActivity.data.dto.TrackDto
import com.example.playlistmaker.SearchActivity.domain.models.Track
import com.example.playlistmaker.SearchActivity.ui.TrackAdapter

interface TrackManager {
    fun getHistoryFromSph(): ArrayList<TrackDto>
    fun clearHistory()
    fun addInHistory(track: TrackDto)
}