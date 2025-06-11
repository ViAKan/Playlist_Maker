package com.example.playlistmaker.media.domain.db

import com.example.playlistmaker.media.data.LikeRepositoryImpl
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class LikesInteractirImpl(private val likesRep: LikesRepository): LikesInteractor {
    override fun listLikes(): Flow<List<Track>> {
        return likesRep.listLikes()
    }

    override suspend fun addToLikes(track: Track) {
        likesRep.addToLikes(track)
    }

    override suspend fun deleteFromLikes(track: Track) {
        likesRep.deleteFromLikes(track)
    }

    override suspend fun check(track: Track): Boolean {
        return likesRep.checkId(track)
    }
}