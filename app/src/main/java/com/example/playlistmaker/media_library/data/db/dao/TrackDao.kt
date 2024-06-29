package com.example.playlistmaker.media_library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTracks(track: TrackEntity)

    @Query("DELETE FROM favourite_track_table WHERE trackId = :trackId")
    suspend fun deleteTrackEntity(trackId: Int)

    @Query("SELECT * FROM favourite_track_table ORDER BY saveDate DESC")
    suspend fun getTracks(): List<TrackEntity>

    @Query("SELECT trackId FROM favourite_track_table")
    suspend fun getIds(): List<Int>
}