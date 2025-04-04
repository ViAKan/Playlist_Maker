package com.example.playlistmaker.search.data.impl

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.TrackRequest
import com.example.playlistmaker.search.data.dto.TrackResponse
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun search(text: String): List<Track>? {
        val response = networkClient.doRequest(TrackRequest(text))
        val code = response.resultCode
        return when (code){
            200 -> {
                val res = (response as TrackResponse).results
                if (res.isNotEmpty()){
                    res.map{
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
                }
                else{
                    emptyList()
                }
            }
            404 ->  emptyList()
            else -> null
        }
    }
}