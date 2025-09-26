package com.example.playlistmaker.media.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.media.domain.db.PlaylistInteractor
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.domain.models.Track
import com.example.playlistmaker.sharing.domain.share.SharingInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistInteractor: PlaylistInteractor, private val sharing: SharingInteractor): ViewModel() {

    private val _playlists = MutableLiveData<MutableList<Playlist>>()
    fun observePlaylists(): LiveData<MutableList<Playlist>> =_playlists

    private val _addTrackResult = MutableLiveData<AddTrackResult>()
    val addTrackResult: LiveData<AddTrackResult> = _addTrackResult

    private val _playlistById = MutableLiveData<Playlist>()
    val playListById: LiveData<Playlist> = _playlistById

    private val _playlistTracks = MutableLiveData<List<Track>>(emptyList())
    val playlistTracks: LiveData<List<Track>> = _playlistTracks

    private val _deletePlaylistResult = MutableLiveData<DeletePlaylistResult>()
    val deletePlaylistResult: LiveData<DeletePlaylistResult> = _deletePlaylistResult

    private val _removeTrackResult = MutableLiveData<RemoveTrackResult>()
    val removeTrackResult: LiveData<RemoveTrackResult> = _removeTrackResult

    private val _updatePlaylistResult = MutableLiveData<UpdatePlaylistResult>()
    val updatePlaylistResult: LiveData<UpdatePlaylistResult> = _updatePlaylistResult

    init {
        loadAllPlaylists()
    }

    fun updatePlaylist(playlistId: Long, name: String, description: String, coverPath: String?) {
        viewModelScope.launch {
            try {
                playlistInteractor.updatePlaylist(playlistId, name, description, coverPath)
                _updatePlaylistResult.postValue(UpdatePlaylistResult.Success)
                loadPlaylistById(playlistId)
                loadAllPlaylists()
            } catch (e: Exception) {
                _updatePlaylistResult.postValue(
                    UpdatePlaylistResult.Error(e.message ?: "Ошибка при обновлении плейлиста")
                )
            }
        }
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

    fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        viewModelScope.launch {
            try {
                if (playlistInteractor.isTrackInPlaylist(playlist.id, track.trackId.toLong())) {
                    _addTrackResult.postValue(AddTrackResult.Error("Трек уже в этом плейлисте"))
                    return@launch
                }

                val success = playlistInteractor.addTrackToPlaylist(playlist.id, track)
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

    fun loadPlaylistById(playlistId: Long) {
        viewModelScope.launch {
            _playlistById.postValue(playlistInteractor.getPlaylistById(playlistId))
        }
    }

    fun fillData(playlistId: Long) {
        viewModelScope.launch {
            playlistInteractor
                .listTracks(playlistId)
                .collect { tracks ->
                    _playlistTracks.postValue(tracks.toMutableList())
                }
        }
    }

    sealed class AddTrackResult {
        object Success : AddTrackResult()
        data class Error(val message: String) : AddTrackResult()
    }

    fun removeTrackFromPlaylist(playlistId: Long, trackId: Long) {
        viewModelScope.launch {
            try {
                playlistInteractor.removeTrackFromPlaylist(playlistId, trackId)
                _removeTrackResult.postValue(RemoveTrackResult.Success)
                loadPlaylistById(playlistId)
                fillData(playlistId)
            } catch (e: Exception) {
                _removeTrackResult.postValue(RemoveTrackResult.Error(e.message ?: "Ошибка при удалении трека"))
            }
        }
    }

    sealed class RemoveTrackResult {
        object Success : RemoveTrackResult()
        data class Error(val message: String) : RemoveTrackResult()
    }

    fun share(playlistName: String, description: String, tracks: List<Track>){
        sharing.sharePlaylist(playlistName,description,tracks)
    }

    fun deletePlaylist(playlistId: Long) {
        viewModelScope.launch {
            try {
                playlistInteractor.deletePlaylist(playlistId)
                _deletePlaylistResult.postValue(DeletePlaylistResult.Success)
                loadAllPlaylists()
            } catch (e: Exception) {
                _deletePlaylistResult.postValue(
                    DeletePlaylistResult.Error(e.message ?: "Ошибка при удалении плейлиста")
                )
            }
        }
    }

    sealed class DeletePlaylistResult {
        object Success : DeletePlaylistResult()
        data class Error(val message: String) : DeletePlaylistResult()
    }

    sealed class UpdatePlaylistResult {
        object Success : UpdatePlaylistResult()
        data class Error(val message: String) : UpdatePlaylistResult()
    }

}
