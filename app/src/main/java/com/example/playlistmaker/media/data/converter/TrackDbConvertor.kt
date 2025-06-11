package com.example.playlistmaker.media.data.converter

import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.search.data.dto.TrackDto
import com.example.playlistmaker.search.domain.models.Track

class TrackDbConvertor {
    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(track.releaseDate, track.primaryGenreName, track.country, track.collectionName, track.trackId, track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.previewUrl)
    }

    fun map(track: TrackEntity): Track {
        return Track(track.releaseDate, track.primaryGenreName, track.country, track.collectionName, track.trackId, track.trackName, track.artistName, track.trackTimeMillis, track.artworkUrl100, track.previewUrl)
    }
}