package com.example.playlistmaker.player.domain.impl

import com.example.playlistmaker.player.domain.mediaplayer.PlayerInteractor
import com.example.playlistmaker.player.domain.mediaplayer.PlayerRepository

class PlayerInteractorImpl(private val repository: PlayerRepository): PlayerInteractor {
    override fun preparePlayer(
        url: String,
        prepareListener: () -> Unit,
        completeListener: () -> Unit
    ) {
        repository.preparePlayer(url, prepareListener, completeListener)
    }

    override fun startPlayer() {
        repository.startPlayer()
    }

    override fun pausePlayer() {
        repository.pausePlayer()
    }

    override fun releasePlayer() {
        repository.releasePlayer()
    }

    override fun getCurrentPosition(): Int {
        return repository.getCurrentPosition()
    }
}