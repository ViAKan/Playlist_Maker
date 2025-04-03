package com.example.playlistmaker.player.di

import com.example.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.player.domain.mediaplayer.PlayerInteractor
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.historyInteractor.HistoryInteractor
import com.example.playlistmaker.search.domain.impl.HistoryInteractorImpl
import com.example.playlistmaker.search.domain.impl.TrackInteractorImpl
import org.koin.dsl.module

val playerInteractorModule = module {

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

}