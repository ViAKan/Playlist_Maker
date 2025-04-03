package com.example.playlistmaker.search.di

import android.content.Context
import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.network.RetrofitNetworkClient
import com.example.playlistmaker.search.data.network.iTunesApi
import com.example.playlistmaker.search.data.sharedPreferences.TrackManager
import com.example.playlistmaker.search.data.sharedPreferences.TrackManagerImpl
import com.example.playlistmaker.search.ui.HISTORY_PREFS
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<iTunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(iTunesApi::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(HISTORY_PREFS, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<TrackManager> {
       TrackManagerImpl(get(), get())
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

}