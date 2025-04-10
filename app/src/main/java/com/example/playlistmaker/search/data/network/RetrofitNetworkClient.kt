package com.example.playlistmaker.search.data.network

import com.example.playlistmaker.search.data.NetworkClient
import com.example.playlistmaker.search.data.dto.Response
import com.example.playlistmaker.search.data.dto.TrackRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient(private val trackService: iTunesApi) : NetworkClient {

//    private val baseUrl = "https://itunes.apple.com"
//
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(baseUrl)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    private val trackService = retrofit.create(iTunesApi::class.java)

    override fun doRequest(dto: Any): Response {
        try{
            if (dto is TrackRequest) {
                val resp = trackService.search(dto.text).execute()
                val body = resp.body() ?: Response()
                if(resp.body()?.results?.isNotEmpty() == true){
                    return body.apply { resultCode = resp.code() }
                } else{
                    return Response().apply { resultCode = 404 }
                }
            }
        } catch(e: Exception){
            return Response().apply { resultCode = 400 }
        }
        return Response()
    }
}