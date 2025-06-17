package com.example.playlistmaker.media.data.converter

import com.example.playlistmaker.media.data.db.PlaylistEntity
import com.example.playlistmaker.media.data.db.TrackInPlaylistEntity
import com.example.playlistmaker.media.data.dto.PlaylistDto
import com.example.playlistmaker.media.domain.model.Playlist
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track

object convert {
    fun PlaylistEntity.toPlaylist() = Playlist(
        id = id,
        name = name,
        description = description,
        coverPath = coverPath,
        tracksJson = tracksJson,
        tracksCount = tracksCount,
        creationDate = creationDate,
        totalDuration = totalDuration
    )
    fun PlaylistDto.toPlaylistEntity() = PlaylistEntity(
        id = id,
        name = name,
        description = description,
        coverPath = coverPath,
        tracksJson = tracksJson,
        tracksCount = tracksCount,
        creationDate = creationDate,
        totalDuration = totalDuration
    )

    fun TrackDto.toTrackInPlaylistEntity() = TrackInPlaylistEntity(
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        trackId = trackId,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl
    )

    fun TrackInPlaylistEntity.toTrack() = Track(
        trackName = trackName,
        artistName = artistName,
        trackTimeMillis = trackTimeMillis,
        artworkUrl100 = artworkUrl100,
        trackId = trackId,
        collectionName = collectionName,
        releaseDate = releaseDate,
        primaryGenreName = primaryGenreName,
        country = country,
        previewUrl = previewUrl
    )
}