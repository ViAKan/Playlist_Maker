package com.example.playlistmaker.media.di

import androidx.room.Room
import com.example.playlistmaker.media.data.db.AppDataBase
import com.example.playlistmaker.media.data.db.PlayDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModuleMedia = module {

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "database.db")
            .build()
    }

    single {
        Room.databaseBuilder(androidContext(), PlayDatabase::class.java, "2database.db")
            .build()
    }

}