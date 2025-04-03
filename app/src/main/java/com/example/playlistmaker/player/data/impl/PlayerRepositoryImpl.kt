package com.example.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.example.playlistmaker.player.domain.mediaplayer.PlayerRepository

class PlayerRepositoryImpl(private var mediaPlayer: MediaPlayer): PlayerRepository {

//    private var mediaPlayer: MediaPlayer = MediaPlayer()

    override fun preparePlayer(
        url: String,
        prepareListener: () -> Unit,
        completeListener: () -> Unit
    ) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            prepareListener()
        }
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.seekTo(0)
            completeListener()
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }

}