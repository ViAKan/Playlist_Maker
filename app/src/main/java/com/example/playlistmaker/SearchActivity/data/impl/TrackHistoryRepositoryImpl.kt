package com.example.playlistmaker.SearchActivity.data.impl

import com.example.playlistmaker.SearchActivity.data.dto.TrackDto
import com.example.playlistmaker.SearchActivity.data.mapper.toTrack
import com.example.playlistmaker.SearchActivity.data.mapper.toTrackDto
import com.example.playlistmaker.SearchActivity.data.sharedPreferences.TrackManager
import com.example.playlistmaker.SearchActivity.domain.api.TrackHistoryRepository
import com.example.playlistmaker.SearchActivity.domain.models.Track
import com.example.playlistmaker.SearchActivity.ui.TrackAdapter

class TrackHistoryRepositoryImpl(val trackManager: TrackManager): TrackHistoryRepository {

    override fun getHistoryFromSph(): ArrayList<Track> {
        val tracksDto = trackManager.getHistoryFromSph()
        return (tracksDto.map {it.toTrack()} as ArrayList<Track>)
    }

    override fun clearHistory() {
        trackManager.clearHistory()
    }

    override fun addInHistory(track: Track) {
        trackManager.addInHistory(track.toTrackDto())
    }
}