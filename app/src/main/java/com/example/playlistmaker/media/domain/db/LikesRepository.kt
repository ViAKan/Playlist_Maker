package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface LikesRepository {

    fun listLikes(): Flow<List<Track>>
    suspend fun addToLikes(track: TrackDto)
    suspend fun deleteFromLikes(track: TrackDto)
    suspend fun updateLikes(trackList: List<TrackDto>)

}