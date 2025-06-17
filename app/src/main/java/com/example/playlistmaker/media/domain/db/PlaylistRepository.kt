package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun createPlaylist(
        name: String,
        description: String,
        coverPath: String?
    )

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(playlistId: Long, trackId: Long)
    suspend fun getPlaylistTracks(playlistId: Long): List<Long>
}