package com.example.playlistmaker.search.domain.impl

import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.api.TrackRepository
import com.example.playlistmaker.search.domain.models.ConsumerData
import com.example.playlistmaker.search.domain.models.Track
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