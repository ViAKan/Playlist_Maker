package com.example.playlistmaker.SearchActivity.domain.impl

import com.example.playlistmaker.SearchActivity.domain.api.TrackInteractor
import com.example.playlistmaker.SearchActivity.domain.api.TrackRepository
import com.example.playlistmaker.SearchActivity.domain.models.ConsumerData
import com.example.playlistmaker.SearchActivity.domain.models.Track
import java.util.concurrent.Executors

class TrackInteractorImpl (private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun search(text: String, consumer: TrackInteractor.TrackConsumer<List<Track>?>) {
        executor.execute{
            val trackList = repository.search(text)
            if(trackList == null){
                consumer.consume(ConsumerData.Error(""))
            } else{
                consumer.consume(ConsumerData.Data(repository.search(text)))
            }
        }
    }
}