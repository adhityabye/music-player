package by.tutorials.musicapp.data.repository

import by.tutorials.musicapp.data.model.Song

interface MusicRepository {
    suspend fun searchSongs(term: String): List<Song>
}
