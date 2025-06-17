package com.example.playlistmaker.media.di

import androidx.room.Room
import com.example.playlistmaker.media.data.db.AppDataBase
import com.example.playlistmaker.media.presentation.LikesViewModel
import com.example.playlistmaker.media.presentation.PlaylistsViewModel
import com.example.playlistmaker.settings.presentation.SettingsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mediaViewModelModule = module {

    viewModel {
        LikesViewModel(androidContext(), get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

}