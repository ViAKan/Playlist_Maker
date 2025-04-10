package com.example.playlistmaker.media.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistsViewModel(val message: String): ViewModel() {
    private val mesLiveData = MutableLiveData(message)
    fun observe(): LiveData<String> = mesLiveData
}