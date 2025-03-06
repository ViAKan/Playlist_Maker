package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.ConsumerData
import com.example.playlistmaker.search.domain.models.Track

interface TrackInteractor {

    fun search(text: String, consumer: TrackConsumer<List<Track>?>)

    interface TrackConsumer<T> {
        fun consume(data: ConsumerData<T>)
    }
}