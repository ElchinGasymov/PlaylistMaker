package com.example.playlistmaker.search.domain

import java.io.Serializable

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val id: Int,
    val collectionName: String,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String,
    val isFavorite: Boolean = false
) : Serializable {
    override fun equals(other: Any?): Boolean {
        return if (other !is Track) {
            false
        } else {
            other.id == id
        }
    }

    override fun hashCode(): Int {
        return id
    }
}
