package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.historyInteractor.HistoryInteractor
import com.example.playlistmaker.search.domain.api.TrackHistoryRepository
import com.example.playlistmaker.search.domain.models.Track

class HistoryInteractorImpl(val repository: TrackHistoryRepository): HistoryInteractor {

    override fun addInHistory(track: Track) {
        repository.addInHistory(track)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun getHistoryFromSph(): ArrayList<Track> {
        return repository.getHistoryFromSph()
    }
}