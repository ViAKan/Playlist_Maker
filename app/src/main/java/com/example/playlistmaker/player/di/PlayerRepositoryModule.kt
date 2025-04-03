package com.example.playlistmaker.player.di

import android.media.MediaPlayer
import com.example.playlistmaker.player.data.impl.PlayerRepositoryImpl
import com.example.playlistmaker.player.domain.mediaplayer.PlayerRepository
import org.koin.dsl.module

val playerRepositoryModule = module {

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    factory<MediaPlayer>{
        MediaPlayer()
    }

}