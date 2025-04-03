package com.example.playlistmaker.sharing.di

import com.example.playlistmaker.sharing.data.navigator.ExternalNavigatorImpl
import com.example.playlistmaker.sharing.data.resources.ResourceProvider
import com.example.playlistmaker.sharing.data.resources.ResourceProviderImpl
import com.example.playlistmaker.sharing.domain.share.ExternalNavigator
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val resourceProviderModule = module {

    single<ResourceProvider> {
        ResourceProviderImpl(androidContext())
    }

}