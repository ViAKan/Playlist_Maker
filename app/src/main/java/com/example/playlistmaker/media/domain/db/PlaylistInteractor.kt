package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun createPlaylist(
        name: String,
        description: String,
        coverPath: String?
    )
    suspend fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Boolean
    suspend fun isTrackInPlaylist(playlistId: Long, trackId: Long): Boolean
    suspend fun getPlaylistById(playlistId: Long): Playlist
    fun listTracks(playlistId: Long): Flow<List<Track>>
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long)
    suspend fun deletePlaylist(playlistId: Long)
    suspend fun updatePlaylist(playlistId: Long, name: String, description: String, coverPath: String?)
}