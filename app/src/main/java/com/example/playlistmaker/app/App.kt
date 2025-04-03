package com.example.playlistmaker.app

import android.app.Application
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.di.playerInteractorModule
import com.example.playlistmaker.player.di.playerRepositoryModule
import com.example.playlistmaker.player.di.playerViewModelModule
import com.example.playlistmaker.search.di.dataModule
import com.example.playlistmaker.search.di.interactorModule
import com.example.playlistmaker.search.di.repositoryModule
import com.example.playlistmaker.search.di.viewModelModule
import com.example.playlistmaker.settings.di.settingsInteractorModule
import com.example.playlistmaker.settings.di.settingsRepositoryModule
import com.example.playlistmaker.settings.di.settingsViewModelModule
import com.example.playlistmaker.settings.domain.sharedPrefs.SwitchThemeInteractor
import com.example.playlistmaker.sharing.di.externalNavigatorModule
import com.example.playlistmaker.sharing.di.resourceProviderModule
import com.example.playlistmaker.sharing.di.sharingInteractorModule
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

const val MODE = "mode"
const val ISDARK = "isDark"
class App : Application() {

    override fun onCreate() {

        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule, playerRepositoryModule, playerInteractorModule, playerViewModelModule, externalNavigatorModule, resourceProviderModule, sharingInteractorModule, settingsRepositoryModule, settingsInteractorModule, settingsViewModelModule)
        }
//        Creator.initApplication(this)
        val switcherInt: SwitchThemeInteractor by inject()
        val key = switcherInt.getSwitchKey()
        switcherInt.switchTheme(key)

    }

}