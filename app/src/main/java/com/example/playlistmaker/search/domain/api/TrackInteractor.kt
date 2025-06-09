package com.example.playlistmaker.search.domain.api

import com.example.playlistmaker.search.domain.models.ConsumerData
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackInteractor {

    fun search(text: String): Flow<ConsumerData<List<Track>?>>

}