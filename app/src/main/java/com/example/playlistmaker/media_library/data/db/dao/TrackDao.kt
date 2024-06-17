package com.example.playlistmaker.media_library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity

@Dao
interface TrackDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE) //метод @Insert для добавления трека в таблицу с избранными треками;
    suspend fun addTracks(track: TrackEntity)

    @Delete(entity = TrackEntity::class) //метод @Delete для удаления трека из таблицы избранных треков;
    suspend fun deleteTrackEntity(trackEntity: TrackEntity)

    @Query("select * FROM favourite_track_table ORDER BY saveDate DESC") //метод @Query для получения списка со всеми треками, добавленными в избранное;
    suspend fun getTracks(): List<TrackEntity>

    @Query("select trackId from favourite_track_table") //метод @Query для получения списка идентификаторов всех треков, которые добавлены в избранное.
    suspend fun getIds(): List<Int>
}