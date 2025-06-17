package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.data.LikeRepositoryImpl
import com.example.playlistmaker.media.data.converter.TrackDbConvertor
import com.example.playlistmaker.media.data.PlaylistRepositoryImpl
import com.example.playlistmaker.media.domain.db.LikesRepository
import com.example.playlistmaker.media.domain.db.PlaylistRepository
import org.koin.dsl.module

val repositoryModuleMedia = module {
    factory { TrackDbConvertor() }

    single<LikesRepository> {
        LikeRepositoryImpl(get(), get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(get(), get())
    }
}