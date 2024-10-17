package com.example.playlistmaker.SearchActivity.data.network

import com.example.playlistmaker.SearchActivity.data.dto.TrackResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface iTunesApi {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String) : retrofit2.Call<TrackResponse>

}