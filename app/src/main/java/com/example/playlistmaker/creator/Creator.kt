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
import com.example.playlistmaker.player.domain.mediaplayer.PlayerRepository
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.network.iTunesApi
import com.example.playlistmaker.search.data.sharedPreferences.TrackManager
import com.example.playlistmaker.settings.domain.sharedPrefs.SwitchThemeRepository
import com.example.playlistmaker.sharing.data.navigator.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.resources.ResourceProvider
import com.example.playlistmaker.sharing.data.resources.ResourceProviderImpl
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.sharing.domain.share.ExternalNavigator
import com.example.playlistmaker.sharing.domain.share.SharingInteractor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object Creator: KoinComponent {

//    lateinit var application: Application
//    private val retrofiClient: NetworkClient by inject()
//    private val trackManager: TrackManager by inject()
    private val trackInteractor: TrackInteractor by inject()
    private val trackHistoryInteractor: HistoryInteractor by inject()
//    private val playerRep: PlayerRepository by inject()
    private val playerInteractor: PlayerInteractor by inject()
    private val extNav: ExternalNavigator by inject()
    private val resPr: ResourceProvider by inject()
    private val sharingInteractor:SharingInteractor by inject()
    private val switchrep: SwitchThemeRepository by inject()
    private val swtchInt: SwitchThemeInteractor by inject()

//    private fun getTrackRepository(): TrackRepository {
//        return TrackRepositoryImpl(retrofiClient)
//    }

    fun provideTrackInteractor(): TrackInteractor {
        return trackInteractor
    }

    fun provideHistoryInteractor(): HistoryInteractor {
        return trackHistoryInteractor
    }

    fun provideSharingInteractor(): SharingInteractor{
        return sharingInteractor
    }

    fun provideSwitcherInteractor(): SwitchThemeInteractor{
        return swtchInt
    }

    fun providePlayerInteractor(): PlayerInteractor{
        return playerInteractor
    }

//    fun initApplication(appl : Application){
//        application = appl
//    }

}