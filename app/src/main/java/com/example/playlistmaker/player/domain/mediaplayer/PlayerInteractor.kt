package com.example.playlistmaker.player.domain.mediaplayer

interface PlayerInteractor{
    fun preparePlayer(url: String, prepareListener: () -> Unit, completeListener: () -> Unit)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition(): Int
}