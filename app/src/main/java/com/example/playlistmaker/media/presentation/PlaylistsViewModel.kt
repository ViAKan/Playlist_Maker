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
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect { playlists ->
                _playlists.value = playlists.toMutableList()
                Log.d("PlaylistsVM", "Playlists updated: ${playlists.size} items")
            }
        }
    }
    private val _addTrackResult = MutableLiveData<AddTrackResult>()
    val addTrackResult: LiveData<AddTrackResult> = _addTrackResult

    fun addTrackToPlaylist(playlist: Playlist, trackId: Long) {
        viewModelScope.launch {
            try {
                if (playlistInteractor.isTrackInPlaylist(playlist.id, trackId)) {
                    _addTrackResult.postValue(AddTrackResult.Error("Трек уже в этом плейлисте"))
                    return@launch
                }

                val success = playlistInteractor.addTrackToPlaylist(playlist.id, trackId)
                if (success) {
                    _addTrackResult.postValue(AddTrackResult.Success)
                    loadAllPlaylists()
                } else {
                    _addTrackResult.postValue(AddTrackResult.Error("Ошибка при добавлении трека"))
                }
            } catch (e: Exception) {
                _addTrackResult.postValue(AddTrackResult.Error(e.message ?: "Неизвестная ошибка"))
            }
        }
    }

    sealed class AddTrackResult {
        object Success : AddTrackResult()
        data class Error(val message: String) : AddTrackResult()
    }
}
