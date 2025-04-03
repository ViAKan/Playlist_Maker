package com.example.playlistmaker.search.di

import com.example.playlistmaker.search.data.impl.TrackHistoryRepositoryImpl
import com.example.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.api.TrackHistoryRepository
import com.example.playlistmaker.search.domain.api.TrackRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<TrackRepository> {
        TrackRepositoryImpl(get())
    }

    single<TrackHistoryRepository> {
        TrackHistoryRepositoryImpl(get())
    }

}