package com.example.playlistmaker.media_library.data.impl

import com.example.playlistmaker.media_library.data.db.AppDatabase
import com.example.playlistmaker.media_library.data.db.PlaylistDbConvertor
import com.example.playlistmaker.media_library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.media_library.data.db.entity.TrackInPlaylistEntity
import com.example.playlistmaker.media_library.domain.db.PlaylistsRepository
import com.example.playlistmaker.new_playlist.domain.model.Playlist
import com.example.playlistmaker.search.domain.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistsRepositoryImpl(
    private val database: AppDatabase,
    private val converter: PlaylistDbConvertor,
) : PlaylistsRepository {

    override suspend fun createPlaylist(playlist: Playlist) {
        database
            .playlistDao()
            .insertPlaylist(converter.map(playlist))
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        database
            .playlistDao()
            .deletePlaylist(converter.map(playlist))
    }

    override suspend fun updateTracks(track: Track, playlist: Playlist) {
        database
            .trackDao()
            .addTrack(TrackInPlaylistEntity.fromDomain(track))

        database
            .playlistDao()
            .updatePlaylist(converter.map(playlist))
    }

    override fun getSavedPlaylists(): Flow<List<Playlist>> {
        return database
            .playlistDao()
            .getSavedPlaylists()
            .map { convertFromTrackEntity(it) }
    }

    private suspend fun convertFromTrackEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        //Эта штука гоняет строку в Json или наоборот
        val gson = Gson()

        //Возвращаем список плейлистов, предварительно преобразовав
        return playlists.map { playlistEntity ->

            //список в который мы положим треки из бд
            val tracksEntity = mutableListOf<TrackInPlaylistEntity>()

            //здесь из json получаем список айдишников List<String>
            val tracksId = gson.fromJson<List<String>>(
                playlistEntity.trackList,
                object : TypeToken<List<String>>() {}.type
            )

            //Проходимся по списку айдишников
            tracksId?.forEach { trackId ->

                //Сходили в БД и получили по trackId конкретный трек(TrackInPlaylistEntity)
                val trackFromBd = database
                    .trackDao()
                    .getTrack(trackId.toInt())

                //добавлем полученный трек в общий список
                tracksEntity.add(trackFromBd)
            }


            converter.map(playlistEntity, tracksEntity)
        }
    }
}