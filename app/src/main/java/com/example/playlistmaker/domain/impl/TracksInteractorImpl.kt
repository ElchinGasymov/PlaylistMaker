package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.repository.TracksRepository
import com.example.playlistmaker.ui.tracks.SearchResult
import java.util.concurrent.*

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(query: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            if (query.isNotEmpty()) {
                consumer.consume(SearchResult.Loading)
                try {
                    val tracks = repository.searchTracks(query)
                    if (tracks.isEmpty()) {
                        consumer.consume(SearchResult.Empty)
                    } else {
                        consumer.consume(SearchResult.Data(tracks))
                    }
                } catch (e: Throwable) {
                    e.printStackTrace()
                    consumer.consume(SearchResult.Error)
                }
            }

        }
    }
}
