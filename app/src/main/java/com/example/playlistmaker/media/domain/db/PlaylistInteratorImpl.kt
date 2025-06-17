package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.data.mapper.toTrackDto
import com.example.playlistmaker.search.domain.models.Track
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

    override suspend fun addTrackToPlaylist(playlistId: Long, track: Track): Boolean {
        return try {
            playlistRepository.addTrackToPlaylist(playlistId, track.toTrackDto())
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun isTrackInPlaylist(playlistId: Long, trackId: Long): Boolean {
        val tracks = playlistRepository.getPlaylistTracks(playlistId)
        return tracks.contains(trackId)
    }

    override suspend fun getPlaylistById(playlistId: Long): Playlist {
        return playlistRepository.getPlaylistById(playlistId)
    }

    override fun listTracks(playlistId: Long): Flow<List<Track>> {
        return playlistRepository.getPlaylistTracksFlow(playlistId)
    }

    override suspend fun removeTrackFromPlaylist(playlistId: Long, trackId: Long) {
        playlistRepository.removeTrackFromPlaylist(playlistId, trackId)

        if (!playlistRepository.isTrackInAnyPlaylist(trackId)) {
            playlistRepository.deleteTrackFromDatabase(trackId)
        }
    }
    override suspend fun deletePlaylist(playlistId: Long) {
        playlistRepository.deletePlaylist(playlistId)
    }
    override suspend fun updatePlaylist(playlistId: Long, name: String, description: String, coverPath: String?) {
        playlistRepository.updatePlaylist(playlistId, name, description, coverPath)
    }
}