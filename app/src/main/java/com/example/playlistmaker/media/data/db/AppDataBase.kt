package com.example.playlistmaker.media.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker.media.data.db.dao.PlaylistDao
import com.example.playlistmaker.media.data.db.dao.TrackDao

@Database(version = 2, entities = [TrackEntity::class], exportSchema = true)
abstract class AppDataBase: RoomDatabase() {
    abstract fun trackDao(): TrackDao
}