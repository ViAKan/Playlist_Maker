package com.example.playlistmaker.player.domain.mediaplayer

interface PlayerRepository {
    fun preparePlayer(url: String, prepareListener: () -> Unit, completeListener: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition(): Int
}