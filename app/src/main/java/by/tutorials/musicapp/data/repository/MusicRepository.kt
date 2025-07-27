package by.tutorials.musicapp.data.repository

import by.tutorials.musicapp.data.model.Song

interface MusicRepository {
    suspend fun search(term: String, limit: Int, offset: Int): List<Song>
}
