package com.example.playlistmaker.media.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media.data.db.dao.PlaylistDao
import com.example.playlistmaker.media.data.db.dao.TrackDao

@Database(version = 1, entities = [PlaylistEntity::class], exportSchema = true)
abstract class PlayDatabase: RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
}