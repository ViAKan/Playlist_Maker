package com.example.playlistmaker.settings.di

import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.historyInteractor.HistoryInteractor
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.settings.domain.impl.SwitchThemeInteractorImpl
import com.example.playlistmaker.settings.domain.sharedPrefs.SwitchThemeInteractor
import org.koin.dsl.module

val settingsInteractorModule = module {

    factory<SwitchThemeInteractor> {
        SwitchThemeInteractorImpl(get())
    }

}