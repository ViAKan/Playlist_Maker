package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.SearchActivity.data.impl.TrackHistoryRepositoryImpl
import com.example.playlistmaker.SearchActivity.data.impl.TrackRepositoryImpl
import com.example.playlistmaker.SearchActivity.data.network.RetrofitNetworkClient
import com.example.playlistmaker.SearchActivity.data.sharedPreferences.TrackManagerImpl
import com.example.playlistmaker.SearchActivity.domain.sharedPrefs.HistoryInteractor
import com.example.playlistmaker.SearchActivity.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.SearchActivity.domain.api.TrackInteractor
import com.example.playlistmaker.SearchActivity.domain.api.TrackRepository
import com.example.playlistmaker.SearchActivity.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.SearchActivity.ui.TrackAdapter

object Creator {
    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(TrackHistoryRepositoryImpl(TrackManagerImpl(context)))
    }
}