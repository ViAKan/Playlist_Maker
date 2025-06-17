package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun createPlaylist(
        name: String,
        description: String,
        coverPath: String?
    )

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(playlistId: Long, track: TrackDto)
    suspend fun getPlaylistTracks(playlistId: Long): List<Long>
    suspend fun getPlaylistById(playlistId: Long): Playlist
    fun getPlaylistTracksFlow(playlistId: Long): Flow<List<Track>>
    suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long)
    suspend fun isTrackInAnyPlaylist(trackId: Long): Boolean
    suspend fun deleteTrackFromDatabase(trackId: Long)
    suspend fun deletePlaylist(playlistId: Long)
    suspend fun updatePlaylist(playlistId: Long, name: String, description: String, coverPath: String?)
}