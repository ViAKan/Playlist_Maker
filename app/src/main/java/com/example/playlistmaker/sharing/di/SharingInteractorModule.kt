package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.settings.domain.impl.SwitchThemeInteractorImpl
import com.example.playlistmaker.settings.domain.sharedPrefs.SwitchThemeInteractor
import com.example.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import com.example.playlistmaker.sharing.domain.share.SharingInteractor
import org.koin.dsl.module

val sharingInteractorModule = module {

    single<SharingInteractor> {
        SharingInteractorImpl(get(), get())
    }

}