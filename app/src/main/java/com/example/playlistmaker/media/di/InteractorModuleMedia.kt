package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.data.LikeRepositoryImpl
import com.example.playlistmaker.media.data.converter.TrackDbConvertor
import com.example.playlistmaker.media.domain.db.LikesInteractirImpl
import com.example.playlistmaker.media.domain.db.LikesInteractor
import com.example.playlistmaker.media.domain.db.LikesRepository
import com.example.playlistmaker.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.media.domain.db.PlaylistInteratorImpl
import org.koin.dsl.module

val interactorModuleMedia = module {
    single<LikesInteractor> {
        LikesInteractirImpl(get())
    }
    single<PlaylistInteractor>{
        PlaylistInteratorImpl(get())
    }
}