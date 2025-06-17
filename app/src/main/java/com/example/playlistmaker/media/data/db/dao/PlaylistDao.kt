package com.example.playlistmaker.media.data.db.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.playlistmaker.media.data.db.PlaylistEntity
import com.example.playlistmaker.media.data.db.TrackEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert
    suspend fun insert(playlist: PlaylistEntity)

    @Update
    suspend fun update(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists ORDER BY name ASC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity

    @Query("SELECT tracksJson FROM playlists WHERE id = :playlistId")
    fun getPlaylistTrackIdsFlow(playlistId: Long): Flow<String>

    @Query("SELECT * FROM playlists WHERE tracksJson LIKE '%' || :trackId || '%'")
    suspend fun getPlaylistsContainingTrack(trackId: Long): List<PlaylistEntity>

    @Delete
    suspend fun deletePlaylistFromData(playlist: PlaylistEntity)

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylist(playlistId: Long)
}