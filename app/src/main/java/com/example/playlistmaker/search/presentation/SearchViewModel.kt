package com.example.playlistmaker.search.presentation

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.historyInteractor.HistoryInteractor
import com.example.playlistmaker.search.domain.models.ConsumerData
import com.example.playlistmaker.search.domain.models.SearchScreenState
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SEARCH_DEBOUNCE_DELAY

class SearchViewModel() : ViewModel() {

    private var searchRunnable: Runnable? = null
    private val handler = Handler(Looper.getMainLooper())

    private val _screenState = MutableLiveData<SearchScreenState>()

    fun getScreenState(): LiveData<SearchScreenState> = _screenState

    private val trackInteractor = Creator.provideTrackInteractor()
    private val historyInteractor: HistoryInteractor = Creator.provideHistoryInteractor()

    fun search(query: String) {
        trackInteractor.search(query, object : TrackInteractor.TrackConsumer<List<Track>?> {
            override fun consume(data: ConsumerData<List<Track>?>) {
                handler.post{
                    when (data) {
                        is ConsumerData.Data -> {
                            val results = data.data ?: emptyList()
                            if(results.isNotEmpty()){
                                _screenState.value = SearchScreenState(trackList = results, error = false, empty = false)
                            }
                            else{
                                _screenState.value = SearchScreenState(empty = true)
                            }
                        }
                        is ConsumerData.Error -> {
                            _screenState.value = SearchScreenState(error = true)
                        }
                    }
                }
            }
        })
    }

    fun searchDebounce(query: String) {
        searchRunnable?.let { handler.removeCallbacks(it) }
        searchRunnable = Runnable { search(query) }
        handler.postDelayed(searchRunnable!!, SEARCH_DEBOUNCE_DELAY)
    }

//    fun addTrackToHistory(track: Track) {
//        historyInteractor.addInHistory(track)
//    }
//
//    fun getHistory(): ArrayList<Track> {
//        return historyInteractor.getHistoryFromSph()
//    }
//
//    fun clearHistory() {
//        historyInteractor.clearHistory()
//    }

    private val _historyLiveData = MutableLiveData<List<Track>>()
    val historyLiveData: LiveData<List<Track>> get() = _historyLiveData

    init {
        loadHistory()
    }

    fun loadHistory() {
        val history = historyInteractor.getHistoryFromSph()
        _historyLiveData.postValue(history)
        Log.d("SearchViewModel", "History loaded: ${history.size} items")
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
        loadHistory()
        Log.d("SearchViewModel", "History cleared")
    }

    fun addTrackToHistory(track: Track) {
        historyInteractor.addInHistory(track)
        loadHistory()
        Log.d("SearchViewModel", "Track added to history: ${track.trackName}")
    }

//    companion object {
//        fun getViewModelFactory(historyInteractor: HistoryInteractor): ViewModelProvider.Factory =
//            object : ViewModelProvider.Factory {
//                @Suppress("UNCHECKED_CAST")
//                override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                    return SearchViewModel(
//                        historyInteractor
//                    ) as T
//                }
//            }
//    }
}