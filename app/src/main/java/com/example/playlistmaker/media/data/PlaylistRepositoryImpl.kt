package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.converter.convert.toPlaylist
import com.example.playlistmaker.media.data.converter.convert.toPlaylistEntity
import com.example.playlistmaker.media.data.db.PlayDatabase
import com.example.playlistmaker.media.data.db.PlaylistEntity
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.domain.db.PlaylistRepository
import com.example.playlistmaker.media.domain.model.Playlist
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val appDatabase: PlayDatabase,
    private val gson: Gson
) : PlaylistRepository {
    override suspend fun createPlaylist(name: String, description: String, coverPath: String?) {
        val playlist = PlaylistDto(
            name = name,
            description = description,
            coverPath = coverPath,
            tracksJson = gson.toJson(emptyList<Long>()),
            tracksCount = 0
        )
        appDatabase.playlistDao().insert(playlist.toPlaylistEntity())
    }

    override  fun getAllPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getAllPlaylists().map { entities -> entities.map{it.toPlaylist()} }
    }
}