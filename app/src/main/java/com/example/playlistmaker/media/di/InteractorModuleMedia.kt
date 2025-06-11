package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.data.LikeRepositoryImpl
import com.example.playlistmaker.media.data.converter.TrackDbConvertor
import com.example.playlistmaker.media.domain.db.LikesInteractirImpl
import com.example.playlistmaker.media.domain.db.LikesInteractor
import com.example.playlistmaker.media.domain.db.LikesRepository
import org.koin.dsl.module

val interactorModuleMedia = module {
    single<LikesInteractor> {
        LikesInteractirImpl(get())
    }
}