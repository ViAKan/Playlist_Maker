package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.data.LikeRepositoryImpl
import com.example.playlistmaker.media.data.converter.TrackDbConvertor
import com.example.playlistmaker.media.domain.db.LikesRepository
import com.example.playlistmaker.search.data.impl.TrackHistoryRepositoryImpl
import com.example.playlistmaker.search.data.impl.TrackRepositoryImpl
import com.example.playlistmaker.search.domain.api.TrackHistoryRepository
import com.example.playlistmaker.search.domain.api.TrackRepository
import org.koin.dsl.module

val repositoryModuleMedia = module {
    factory { TrackDbConvertor() }

    single<LikesRepository> {
        LikeRepositoryImpl(get(), get())
    }
}