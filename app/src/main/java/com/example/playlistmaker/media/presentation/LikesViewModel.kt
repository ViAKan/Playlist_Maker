package com.example.playlistmaker.media.presentation

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.media.domain.db.LikesInteractor
import com.example.playlistmaker.media.domain.model.LikesState
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LikesViewModel(private val context: Context, private val likeInteractor: LikesInteractor): ViewModel() {

    private val stateLiveData = MutableLiveData<MutableList<Track>>()

    fun observeState(): LiveData<MutableList<Track>> = stateLiveData

    init {
       fillData()
    }

    fun fillData() {
        viewModelScope.launch {
            likeInteractor
                .listLikes()
                .collect { tracks ->
                    stateLiveData.postValue(tracks.toMutableList())
                }
        }
    }
//    private fun processResult(tracks: List<Track>) {
//        if (tracks.isEmpty()) {
//            renderState(LikesState.Empty(context.getString(R.string.media_empty)))
//        } else {
//            renderState(LikesState.Content(tracks))
//        }
//    }
//
//    private fun renderState(state: LikesState) {
//        stateLiveData.postValue(state)
//    }
}