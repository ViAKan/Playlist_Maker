package com.example.playlistmaker.creator

import android.app.Application
import android.content.Context
import com.example.playlistmaker.search.data.impl.TrackHistoryRepositoryImpl
import com.example.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.sharedPreferences.TrackManagerImpl
import com.example.playlistmaker.search.domain.historyInteractor.HistoryInteractor
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.SettingsActivity.data.impl.SwitchThemeRepositoryImpl
import com.example.playlistmaker.SettingsActivity.domain.impl.SwitchThemeInteractorImpl
import com.example.playlistmaker.SettingsActivity.domain.sharedPrefs.SwitchThemeInteractor
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.mediaplayer.PlayerInteractor
import com.example.playlistmaker.search.ui.SearchActivity

object Creator {

    lateinit var application: Application

    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient())
    }

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository())
    }

    fun provideHistoryInteractor(): HistoryInteractor {
        return HistoryInteractorImpl(TrackHistoryRepositoryImpl(TrackManagerImpl(application)))
    }

    fun provideSwitcherInteractor(): SwitchThemeInteractor{
        return SwitchThemeInteractorImpl(SwitchThemeRepositoryImpl(application))
    }

    fun providePlayerInteractor(): PlayerInteractor{
        return PlayerInteractorImpl(PlayerRepositoryImpl())
    }

    fun initApplication(appl : Application){
        application = appl
    }
}