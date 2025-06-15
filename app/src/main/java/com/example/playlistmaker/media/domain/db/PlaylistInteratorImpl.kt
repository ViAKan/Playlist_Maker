package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteratorImpl(
    private val playlistRepository: PlaylistRepository
) : PlaylistInteractor {

    override suspend fun createPlaylist(
        name: String,
        description: String,
        coverPath: String?
    ) {
         playlistRepository.createPlaylist(name, description, coverPath)
    }

    override suspend fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getAllPlaylists()
    }
}