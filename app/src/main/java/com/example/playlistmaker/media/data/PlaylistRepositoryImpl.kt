package com.example.playlistmaker.media.data

import android.util.Log
import com.example.playlistmaker.media.data.converter.convert.toPlaylist
import com.example.playlistmaker.media.data.converter.convert.toPlaylistEntity
import com.example.playlistmaker.media.data.converter.convert.toTrack
import com.example.playlistmaker.media.data.converter.convert.toTrackInPlaylistEntity
import com.example.playlistmaker.media.data.db.PlayDatabase
import com.example.playlistmaker.media.data.db.PlaylistEntity
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.data.db.TrackInPlaylistEntity
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.domain.db.PlaylistRepository
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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
            tracksCount = 0,
            creationDate = System.currentTimeMillis(),
            totalDuration = 0
        )
        appDatabase.playlistDao().insert(playlist.toPlaylistEntity())
    }

    override  fun getAllPlaylists(): Flow<List<Playlist>> {
        return appDatabase.playlistDao().getAllPlaylists().map { entities -> entities.map{it.toPlaylist()} }
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, track: TrackDto) {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        val tracks = gson.fromJson<List<Long>>(playlist.tracksJson, object : TypeToken<List<Long>>() {}.type)
        val updatedTracks = tracks.toMutableList().apply { add(track.trackId.toLong()) }
        val updatedPlaylist = playlist.copy(
            tracksJson = gson.toJson(updatedTracks),
            tracksCount = updatedTracks.size,
        )
        appDatabase.playlistDao().update(updatedPlaylist)
        appDatabase.tracksInlaylistDao().insertTracks(track.toTrackInPlaylistEntity())
    }

    override suspend fun getPlaylistTracks(playlistId: Long): List<Long> {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        return gson.fromJson(playlist.tracksJson, object : TypeToken<List<Long>>() {}.type)
    }

    override suspend fun getPlaylistById(playlistId: Long): Playlist {
        return appDatabase.playlistDao().getPlaylistById(playlistId).toPlaylist()
    }

    override fun getPlaylistTracksFlow(playlistId: Long): Flow<List<Track>> {
        return appDatabase.playlistDao().getPlaylistTrackIdsFlow(playlistId)
            .map { json ->
                gson.fromJson<List<Long>>(json, object : TypeToken<List<Long>>() {}.type)
            }
            .flatMapLatest { trackIds ->
                if (trackIds.isEmpty()) {
                    flowOf(emptyList<TrackInPlaylistEntity>())
                } else {
                    appDatabase.tracksInlaylistDao().getPlaylistTracks(trackIds)
                }
            }
            .map { trackEntities ->
                trackEntities.map { it.toTrack() }
            }
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long) {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        val tracks = gson.fromJson<List<Long>>(playlist.tracksJson, object : TypeToken<List<Long>>() {}.type)
        val updatedTracks = tracks.toMutableList().apply { remove(trackId) }

        val updatedPlaylist = playlist.copy(
            tracksJson = gson.toJson(updatedTracks),
            tracksCount = updatedTracks.size,
        )
        appDatabase.playlistDao().update(updatedPlaylist)
    }

    override suspend fun isTrackInAnyPlaylist(trackId: Long): Boolean {
        return appDatabase.playlistDao().getPlaylistsContainingTrack(trackId).isNotEmpty()
    }

    override suspend fun deleteTrackFromDatabase(trackId: Long) {
        appDatabase.tracksInlaylistDao().deleteTrackById(trackId)
    }

    override suspend fun deletePlaylist(playlistId: Long) {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        val trackIds = gson.fromJson<List<Long>>(
            playlist.tracksJson,
            object : TypeToken<List<Long>>() {}.type
        )

        trackIds.forEach { trackId ->
            val playlistsWithTrack = appDatabase.playlistDao().getPlaylistsContainingTrack(trackId)
            val otherPlaylistsWithTrack = playlistsWithTrack.filter { it.id != playlistId }
            if (otherPlaylistsWithTrack.isEmpty()) {
                appDatabase.tracksInlaylistDao().deleteTrackById(trackId)
            }
        }

        appDatabase.playlistDao().deletePlaylistFromData(playlist)
    }
    override suspend fun updatePlaylist(playlistId: Long, name: String, description: String, coverPath: String?) {
        val playlist = appDatabase.playlistDao().getPlaylistById(playlistId)
        val updatedPlaylist = playlist.copy(
            name = name,
            description = description,
            coverPath = coverPath
        )
        appDatabase.playlistDao().update(updatedPlaylist)
    }
}