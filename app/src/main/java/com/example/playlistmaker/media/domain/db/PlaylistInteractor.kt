package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun createPlaylist(
        name: String,
        description: String,
        coverPath: String?
    )
    suspend fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(playlistId: Long, trackId: Long): Boolean
    suspend fun isTrackInPlaylist(playlistId: Long, trackId: Long): Boolean
}