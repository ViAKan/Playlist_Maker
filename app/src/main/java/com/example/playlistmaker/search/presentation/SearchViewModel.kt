package com.example.playlistmaker.search.presentation

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.api.TrackInteractor
import com.example.playlistmaker.search.domain.historyInteractor.HistoryInteractor
import com.example.playlistmaker.search.domain.models.ConsumerData
import com.example.playlistmaker.search.domain.models.SearchScreenState
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.search.ui.SEARCH_DEBOUNCE_DELAY
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class SearchViewModel(private val trackInteractor: TrackInteractor, private val historyInteractor: HistoryInteractor) : ViewModel() {

//    private var searchRunnable: Runnable? = null
    private val handler = Handler(Looper.getMainLooper())

    private val _screenState = MutableLiveData<SearchScreenState>()

    fun getScreenState(): LiveData<SearchScreenState> = _screenState

    private var latestSearchText: String? = null

    private var searchJob: Job? = null

    fun search(query: String) {
        viewModelScope.launch {
            trackInteractor.search(query)
                .flowOn(Dispatchers.IO)
                .collect { data ->
                    when (data) {
                        is ConsumerData.Data -> {
                            val results = data.data ?: emptyList()
                            _screenState.value = if (results.isNotEmpty()) {
                                SearchScreenState(trackList = results, error = false, empty = false)
                            } else {
                                SearchScreenState(empty = true)
                            }
                        }
                        is ConsumerData.Error -> {
                            _screenState.value = SearchScreenState(error = true)
                        }
                    }
                }
        }
    }

    fun searchDebounce(query: String) {
        if (latestSearchText == query) {
            return
        }

        latestSearchText = query

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            search(query)
        }
    }

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

}