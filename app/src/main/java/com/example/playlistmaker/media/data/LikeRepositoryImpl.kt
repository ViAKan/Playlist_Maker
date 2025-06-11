package com.example.playlistmaker.media.data

import com.example.playlistmaker.media.data.converter.TrackDbConvertor
import com.example.playlistmaker.media.data.db.AppDataBase
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.domain.db.LikesRepository
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class LikeRepositoryImpl(private val appDataBase: AppDataBase, private val trackConvertor: TrackDbConvertor): LikesRepository {

    override fun listLikes(): Flow<List<Track>>{
        val tracks = appDataBase.trackDao().getLikedTracks()
        return tracks.map{ trackEntities ->
            trackEntities.map {trackConvertor.map(it) }
        }
    }

    override suspend fun addToLikes(track: TrackDto) {
        val trackEnt = trackConvertor.map(track)
        appDataBase.trackDao().insertTracks(trackEnt)
    }

    override suspend fun deleteFromLikes(track: TrackDto) {
        val trackEnt = trackConvertor.map(track)
        appDataBase.trackDao().deleteFromLikes(trackEnt)
    }

    override suspend fun isTrackLiked(trackId: Int): Boolean {
        return appDataBase.trackDao().isTrackLiked(trackId) > 0
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackConvertor.map(track) }
    }


}