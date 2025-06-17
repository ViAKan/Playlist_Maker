package com.example.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media.data.db.dao.PlaylistDao
import com.example.playlistmaker.media.data.db.dao.TrackDao
import com.example.playlistmaker.media.data.db.dao.TracksInPlaylistDao

@Database(version = 2, entities = [PlaylistEntity::class, TrackInPlaylistEntity::class], exportSchema = true)
abstract class PlayDatabase: RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun tracksInlaylistDao(): TracksInPlaylistDao
}