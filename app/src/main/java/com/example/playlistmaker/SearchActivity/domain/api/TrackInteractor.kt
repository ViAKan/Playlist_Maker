package com.example.playlistmaker.SearchActivity.domain.api

import com.example.playlistmaker.SearchActivity.domain.models.ConsumerData
import com.example.playlistmaker.SearchActivity.domain.models.Track

interface TrackInteractor {

    fun search(text: String, consumer: TrackConsumer<List<Track>?>)

    interface TrackConsumer<T> {
        fun consume(data: ConsumerData<T>)
    }
}