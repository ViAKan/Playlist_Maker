package com.example.playlistmaker.media.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media.data.db.TrackEntity
import com.example.playlistmaker.media.data.db.TrackInPlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TracksInPlaylistDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTracks(track: TrackInPlaylistEntity)

    @Query("SELECT * FROM track_playlists WHERE trackId IN (:trackIds) ORDER BY timestamp DESC")
    fun getPlaylistTracks(trackIds: List<Long>): Flow<List<TrackInPlaylistEntity>>

    @Delete
    suspend fun deleteTrack(track: TrackInPlaylistEntity)

    @Query("DELETE FROM track_playlists WHERE trackId = :trackId")
    suspend fun deleteTrackById(trackId: Long)
}