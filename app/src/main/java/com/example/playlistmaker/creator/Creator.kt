package com.example.playlistmaker.creator

import android.app.Application
import com.example.playlistmaker.search.data.impl.TrackHistoryRepositoryImpl
import com.example.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.sharedPreferences.TrackManagerImpl
import com.example.playlistmaker.search.domain.historyInteractor.HistoryInteractor
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.settings.data.impl.SwitchThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.impl.SwitchThemeInteractorImpl
import com.example.playlistmaker.settings.domain.sharedPrefs.SwitchThemeInteractor
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.mediaplayer.PlayerInteractor
import com.example.playlistmaker.sharing.data.navigator.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.resources.ResourceProviderImpl
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.sharing.domain.share.SharingInteractor

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

    fun provideSharingInteractor(): SharingInteractor{
        return SharingInteractorImpl(ExternalNavigatorImpl(application), ResourceProviderImpl(
            application))
    }

//    fun provideNavigator(context: Context): ExternalNavigator{
//        return ExternalNavigatorImpl(context)
//    }


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