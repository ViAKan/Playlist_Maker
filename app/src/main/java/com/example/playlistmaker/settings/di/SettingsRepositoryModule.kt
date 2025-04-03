package com.example.playlistmaker.settings.di

import android.content.Context
import com.example.playlistmaker.search.data.impl.TrackHistoryRepositoryImpl
import com.example.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.api.TrackHistoryRepository
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.ui.HISTORY_PREFS
import com.example.playlistmaker.settings.data.impl.SwitchThemeRepositoryImpl
import com.example.playlistmaker.settings.domain.sharedPrefs.SwitchThemeRepository
import com.example.playlistmaker.settings.ui.THEME_PREFERENCES
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val settingsRepositoryModule = module {

    single {
        androidContext()
            .getSharedPreferences(THEME_PREFERENCES, Context.MODE_PRIVATE)
    }

    factory<SwitchThemeRepository> {
        SwitchThemeRepositoryImpl(get())
    }

}