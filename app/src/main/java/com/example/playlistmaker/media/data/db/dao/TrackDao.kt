package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.media.data.db.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTracks(track: TrackEntity)

    @Query("SELECT * FROM track_table ORDER BY timestamp DESC")
    fun getLikedTracks(): Flow<List<TrackEntity>>

    @Query("SELECT trackId FROM track_table")
    suspend fun getLikedTracksId(): List<Int>

    @Delete(entity = TrackEntity::class)
    suspend fun deleteFromLikes(track: TrackEntity)

    @Query("SELECT COUNT(*) FROM track_table WHERE trackId = :trackId")
    suspend fun isTrackLiked(trackId: Int): Int
}