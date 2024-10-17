package com.example.playlistmaker.SearchActivity.domain.impl

import com.example.playlistmaker.SearchActivity.domain.sharedPrefs.HistoryInteractor
import com.example.playlistmaker.SearchActivity.domain.api.TrackHistoryRepository
import com.example.playlistmaker.SearchActivity.domain.models.Track

class HistoryInteractorImpl(val repository: TrackHistoryRepository): HistoryInteractor {

    override fun addInHistory(track: Track, historyList: ArrayList<Track>) {
        repository.addInHistory(track, historyList)
    }

    override fun clearHistory() {
        repository.clearHistory()
    }

    override fun getHistoryFromSph(): ArrayList<Track> {
        return repository.getHistoryFromSph()
    }
}