package com.example.playlistmaker.media.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val _playlists = MutableLiveData<MutableList<Playlist>>()
    fun observePlaylists(): LiveData<MutableList<Playlist>> =_playlists

    init {
        loadAllPlaylists()
    }

    fun createPlaylist(name: String, description: String, coverPath: String?) {
        viewModelScope.launch {
            try {
                playlistInteractor.createPlaylist(name, description, coverPath)
            } catch (e: Exception) {
                // Обработка ошибок
            }
        }
    }

    fun loadAllPlaylists() {
//        viewModelScope.launch {
//            try {
//                val playlists = playlistInteractor.getAllPlaylists()
//                _playlists.postValue(playlists.toMutableList())
//                Log.d("PlaylistsViewModel", "Loaded playlists: ${playlists.size}")
//                playlists.forEach { playlist ->
//                    Log.d("PlaylistsViewModel",
//                        "Playlist: id=${playlist.id}, name=${playlist.name}, " +
//                                "tracks=${playlist.tracksCount}")
//                }
//            } catch (e: Exception) {
//                Log.e("PlaylistsViewModel", "Error loading playlists", e)
//            }
//        }
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect { playlists ->
                _playlists.value = playlists.toMutableList()
                Log.d("PlaylistsVM", "Playlists updated: ${playlists.size} items")
            }
        }
    }
}
