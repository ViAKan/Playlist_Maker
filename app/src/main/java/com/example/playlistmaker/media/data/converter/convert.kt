package com.example.playlistmaker.media.data.converter

import com.example.playlistmaker.media.data.db.PlaylistEntity
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.domain.model.Playlist

object convert {
    fun PlaylistEntity.toPlaylist() = Playlist(
        id = id,
        name = name,
        description = description,
        coverPath = coverPath,
        tracksJson = tracksJson,
        tracksCount = tracksCount
    )
    fun PlaylistDto.toPlaylistEntity() = PlaylistEntity(
        id = id,
        name = name,
        description = description,
        coverPath = coverPath,
        tracksJson = tracksJson,
        tracksCount = tracksCount
    )
}