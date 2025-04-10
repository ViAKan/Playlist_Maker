package com.example.playlistmaker.media.di

import com.example.playlistmaker.media.presentation.LikesViewModel
import com.example.playlistmaker.media.presentation.PlaylistsViewModel
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaViewModelModule = module {

    viewModel {(mes: String) ->
        LikesViewModel(mes)
    }

    viewModel {(mes: String) ->
        PlaylistsViewModel(mes)
    }

}