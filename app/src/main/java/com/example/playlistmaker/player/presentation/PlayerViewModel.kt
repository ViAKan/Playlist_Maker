package com.example.playlistmaker.player.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.search.domain.models.SearchScreenState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerViewModel: ViewModel() {

    private val player = Creator.providePlayerInteractor()
    private val dateFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }
    private val handler: Handler? = Handler(Looper.getMainLooper())

    private val _currentTime = MutableLiveData<String>()
    private val _playerState = MutableLiveData<Int>()
    private val _isPlayEnabled = MutableLiveData<Boolean>()

    fun getTime(): LiveData<String> = _currentTime
    fun getPlayerState(): LiveData<Int> = _playerState
    fun getPlayEnabled(): LiveData<Boolean> = _isPlayEnabled

    init {
        _playerState.value = STATE_DEFAULT
        _currentTime.value = "00:00"
        _isPlayEnabled.value = false
    }

    fun preparePlayer(url: String) {
        player.preparePlayer(url, { onPrepare() }, { onComplete() })
    }

    fun playbackControl() {
        when (_playerState.value) {
            STATE_PLAYING -> pausePlayer()
            STATE_PREPARED, STATE_PAUSED -> startPlayer()
        }
    }

    private fun startPlayer() {
        player.startPlayer()
        _playerState.value = STATE_PLAYING
        handler?.post(updateTimer())
    }

    fun pausePlayer() {
        player.pausePlayer()
        _playerState.value = STATE_PAUSED
        handler?.removeCallbacks(updateTimer())
    }

    private fun onPrepare() {
        _currentTime.value = "00:00"
        _isPlayEnabled.value = true
        _playerState.value = STATE_PREPARED
    }

    private fun onComplete() {
        handler?.removeCallbacksAndMessages(null)
        _currentTime.value = "00:00"
        _playerState.value = STATE_PREPARED
    }

    private fun updateTimer(): Runnable {
        return object : Runnable {
            override fun run() {
                _currentTime.value = dateFormat.format(player.getCurrentPosition())
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
