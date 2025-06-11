package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface LikesInteractor {

    fun listLikes(): Flow<List<Track>>
    suspend fun addToLikes(track: Track)
    suspend fun deleteFromLikes(track: Track)
    suspend fun update(trackList: List<Track>)
}