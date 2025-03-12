package com.example.playlistmaker.player.domain.model

data class PlayerState (
    val currentTime: String = "",
    val state: Int = 0,
    val isPlayEnabled: Boolean = false
)