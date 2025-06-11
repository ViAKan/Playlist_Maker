package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackRequest
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun search(text: String): Flow<List<Track>?> = flow{
        val response = networkClient.doRequest(TrackRequest(text))
        val code = response.resultCode
        when (code){
            200 -> {
                val res = (response as TrackResponse).results
                if (res.isNotEmpty()){
                    val data = res.map{
                        Track(
                        it.releaseDate,
                        it.primaryGenreName,
                        it.country,
                        it.collectionName,
                        it.trackId,
                        it.trackName,
                        it.artistName,
                        it.trackTimeMillis,
                        it.artworkUrl100,
                        it.previewUrl
                    )}
                    emit(data)
                }
                else{
                    emit(emptyList())
                }
            }
            404 ->  emit(emptyList())
            else -> emit(null)
        }
    }
}