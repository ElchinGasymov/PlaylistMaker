package com.example.playlistmaker.media_library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.playlistmaker.media_library.data.db.dao.FavouriteTrackDao
import com.example.playlistmaker.media_library.data.db.dao.PlaylistDao
import com.example.playlistmaker.media_library.data.db.dao.TrackDao
import com.example.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media_library.data.db.entity.TrackEntity
import com.example.playlistmaker.media_library.data.db.entity.TrackInPlaylistEntity

@Database(
    version = 2, entities = [
        TrackEntity::class,
        PlaylistEntity::class,
        TrackInPlaylistEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favouriteTrackDao(): FavouriteTrackDao
    abstract fun playlistDao(): PlaylistDao
    abstract fun trackDao(): TrackDao
}