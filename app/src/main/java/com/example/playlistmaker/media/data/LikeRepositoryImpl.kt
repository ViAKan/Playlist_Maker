package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.converter.TrackDbConvertor
import com.example.playlistmaker.media.data.db.AppDataBase
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.db.LikesRepository
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LikeRepositoryImpl(private val appDataBase: AppDataBase, private val trackConvertor: TrackDbConvertor): LikesRepository {
    override fun listLikes(): Flow<List<Track>> = flow {
        val tracks = appDataBase.trackDao().getLikedTracks()
        emit(convertFromTrackEntity(tracks))
    }

    override suspend fun addToLikes(track: Track) {
        val trackEnt = trackConvertor.map(track)
        appDataBase.trackDao().insertTracks(trackEnt)
    }

    override suspend fun deleteFromLikes(track: Track) {
        val trackEnt = trackConvertor.map(track)
        appDataBase.trackDao().deleteFromLikes(trackEnt)
    }

    override suspend fun checkId(track: Track): Boolean {
        val trackListIds = appDataBase.trackDao().getLikedTracksId()
        if(trackListIds.contains(track.trackId)) return true
        return false
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackConvertor.map(track) }
    }


}