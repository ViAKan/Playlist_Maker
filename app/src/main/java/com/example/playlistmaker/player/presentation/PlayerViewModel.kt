package com.example.playlistmaker.player.presentation

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.media.domain.db.LikesInteractor
import com.example.playlistmaker.player.domain.mediaplayer.PlayerInteractor
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.search.domain.models.SearchScreenState
import com.example.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val player: PlayerInteractor, private val likeInteractor: LikesInteractor): ViewModel() {

    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    private val playerState = MutableLiveData<PlayerState>()
    private val isFavorite = MutableLiveData<Boolean>()

    fun getState(): LiveData<PlayerState> = playerState
    fun getLike(): LiveData<Boolean> = isFavorite

    private var timerJob: Job? = null

    init {
        playerState.value = PlayerState(currentTime = "00:00", state = STATE_DEFAULT, isPlayEnabled = false)
    }

    fun preparePlayer(url: String) {
        player.preparePlayer(url, { onPrepare() }, { onComplete() })
    }

    fun playbackControl() {
        when (playerState.value?.state) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }

    private fun startPlayer() {
        player.startPlayer()
        playerState.value = playerState.value?.copy(state = STATE_PLAYING)
        startTimer()
    }

    fun pausePlayer() {
        player.pausePlayer()
        timerJob?.cancel()
        playerState.value = playerState.value?.copy(state = STATE_PAUSED)
    }

    private fun onPrepare() {
        playerState.value = PlayerState(currentTime = "00:00", state = STATE_PREPARED, isPlayEnabled = true)
    }

    private fun onComplete() {
        playerState.value = playerState.value?.copy(currentTime = "00:00", state = STATE_PREPARED)
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerState.value?.state == STATE_PLAYING) {
                delay(PLAY_DELAY)
                playerState.value = playerState.value?.copy(currentTime = dateFormat.format(player.getCurrentPosition()))
            }
        }
    }

    fun addToLikesOrRemove(track: Track){
        viewModelScope.launch {
            isFavorite.postValue(likeInteractor.check(track))
            Log.d("PlayerVM", "Current trackId: ${track.trackId}")
            val newFavoriteState = likeInteractor.check(track)
            if (newFavoriteState == false) {
                likeInteractor.addToLikes(track.copy(isFavorite = true))
                Log.d("PlayerVM", "Is favorite: ${likeInteractor.check(track)}")
            } else {
                likeInteractor.deleteFromLikes(track)
                track.isFavorite = false
                Log.d("PlayerVMDel", "Is favorite: ${likeInteractor.check(track)}")
            }
            isFavorite.postValue(newFavoriteState)
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.releasePlayer()
    }

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val PLAY_DELAY = 300L
    }

}
