package com.example.playlistmaker.player.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.player.domain.mediaplayer.PlayerInteractor
import com.example.playlistmaker.player.domain.model.PlayerState
import com.example.playlistmaker.search.domain.models.SearchScreenState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel(private val player: PlayerInteractor): ViewModel() {

//    private val player = Creator.providePlayerInteractor()
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val handler: Handler? = Handler(Looper.getMainLooper())

    private val playerState = MutableLiveData<PlayerState>()

    fun getState(): LiveData<PlayerState> = playerState

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
        handler?.post(updateTimer())
    }

    fun pausePlayer() {
        player.pausePlayer()
        playerState.value = playerState.value?.copy(state = STATE_PAUSED)
        handler?.removeCallbacks(updateTimer())
    }

    private fun onPrepare() {
        playerState.value = PlayerState(currentTime = "00:00", state = STATE_PREPARED, isPlayEnabled = true)
    }

    private fun onComplete() {
        handler?.removeCallbacksAndMessages(null)
        playerState.value = playerState.value?.copy(currentTime = "00:00", state = STATE_PREPARED)
    }

    private fun updateTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                playerState.value = playerState.value?.copy(currentTime = dateFormat.format(player.getCurrentPosition()))
                handler?.postDelayed(this, PLAY_DELAY)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.releasePlayer()
        handler?.removeCallbacksAndMessages(null)
    }

    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val PLAY_DELAY = 300L
    }

}
