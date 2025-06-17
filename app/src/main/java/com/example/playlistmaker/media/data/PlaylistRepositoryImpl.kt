package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.converter.convert.toPlaylist
import com.example.playlistmaker.media.data.converter.convert.toPlaylistEntity
import com.example.playlistmaker.media.data.db.PlayDatabase
import com.example.playlistmaker.media.data.db.PlaylistEntity
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.domain.db.PlaylistRepository
import com.example.playlistmaker.media.domain.model.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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

    override suspend fun addTrackToPlaylist(playlistId: Long, trackId: Long) {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        val tracks = gson.fromJson<List<Long>>(playlist.tracksJson, object : TypeToken<List<Long>>() {}.type)
        val updatedTracks = tracks.toMutableList().apply { add(trackId) }
        val updatedPlaylist = playlist.copy(
            tracksJson = gson.toJson(updatedTracks),
            tracksCount = updatedTracks.size
        )
        appDatabase.playlistDao().update(updatedPlaylist)
    }

    override suspend fun getPlaylistTracks(playlistId: Long): List<Long> {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        return gson.fromJson(playlist.tracksJson, object : TypeToken<List<Long>>() {}.type)
    }
}