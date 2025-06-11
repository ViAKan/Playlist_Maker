package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.media.data.db.AppDataBase
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.data.mapper.toTrack
import com.example.playlistmaker.search.data.mapper.toTrackDto
import com.example.playlistmaker.search.data.sharedPreferences.TrackManager
import com.example.playlistmaker.search.domain.api.TrackHistoryRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class TrackHistoryRepositoryImpl(val trackManager: TrackManager, private val appDataBase: AppDataBase): TrackHistoryRepository {

    override fun getHistoryFromSph(): ArrayList<Track>  = runBlocking {
        val tracksDto = trackManager.getHistoryFromSph()
        val likedTrackIds = withContext(Dispatchers.IO) {
            appDataBase.trackDao().getLikedTracksId()
        }

        tracksDto.map { dto ->
            dto.toTrack().copy(
                isFavorite = likedTrackIds.contains(dto.trackId)
            )
        } as ArrayList<Track>
    }

    override fun clearHistory() {
        trackManager.clearHistory()
    }

    override fun addInHistory(track: Track) {
        trackManager.addInHistory(track.toTrackDto())
    }

}