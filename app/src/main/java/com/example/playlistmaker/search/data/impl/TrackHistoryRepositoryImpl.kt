package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.mapper.toTrack
import com.example.playlistmaker.search.data.mapper.toTrackDto
import com.example.playlistmaker.search.data.sharedPreferences.TrackManager
import com.example.playlistmaker.search.domain.api.TrackHistoryRepository
import com.example.playlistmaker.search.domain.models.Track

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