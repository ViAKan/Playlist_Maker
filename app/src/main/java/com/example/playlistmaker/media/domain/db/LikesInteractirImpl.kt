package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.data.LikeRepositoryImpl
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.data.mapper.toTrackDto
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class LikesInteractirImpl(private val likesRep: LikesRepository): LikesInteractor {

    override fun listLikes(): Flow<List<Track>> {
        return likesRep.listLikes()
    }

    override suspend fun addToLikes(track: Track) {
        likesRep.addToLikes(TrackDto(track.releaseDate, track.primaryGenreName, track.country, track.collectionName, track.trackId, track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.artworkUrl100))
    }

    override suspend fun deleteFromLikes(track: Track) {
        likesRep.deleteFromLikes(TrackDto(track.releaseDate, track.primaryGenreName, track.country, track.collectionName, track.trackId, track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.artworkUrl100))
    }

    override suspend fun isTrackLiked(trackId: Int): Boolean {
        return likesRep.isTrackLiked(trackId)
    }

}