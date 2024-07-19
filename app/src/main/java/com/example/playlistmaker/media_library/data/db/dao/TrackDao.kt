package com.example.playlistmaker.media_library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media_library.data.db.entity.TrackInPlaylistEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrack(track: TrackInPlaylistEntity)

    @Query("DELETE FROM favourite_track_table WHERE trackId = :trackId")
    suspend fun deleteTrackEntity(trackId: Int)

    @Query("SELECT * FROM track_in_playlist_entity WHERE trackId = :id")
    suspend fun getTrack(id: Int): TrackInPlaylistEntity

    @Query("SELECT trackId FROM favourite_track_table")
    suspend fun getIds(): List<Int>
}