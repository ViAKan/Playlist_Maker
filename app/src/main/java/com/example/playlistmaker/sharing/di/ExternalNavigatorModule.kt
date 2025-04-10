package com.example.playlistmaker.sharing.di

import android.content.Context
import com.example.playlistmaker.settings.data.impl.SwitchThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.sharedPrefs.SwitchThemeRepository
import com.example.playlistmaker.settings.ui.THEME_PREFERENCES
import com.example.playlistmaker.sharing.data.navigator.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.domain.share.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val externalNavigatorModule = module {

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

}