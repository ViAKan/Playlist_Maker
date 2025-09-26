package com.example.playlistmaker.media.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Playlist (
    val id: Long = 0,
    val name: String,
    val description: String,
    val coverPath: String?,
    val tracksJson: String,
    val tracksCount: Int,
    val creationDate: Long,
    val totalDuration: Long
): Parcelable