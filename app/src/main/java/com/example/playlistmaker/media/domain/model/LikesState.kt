package com.example.playlistmaker.media.domain.model

import com.example.playlistmaker.search.domain.models.Track

sealed interface LikesState {

    data class Content(
        val tracks: List<Track>
    ) : LikesState

    data class Empty(
        val message: String
    ) : LikesState
}