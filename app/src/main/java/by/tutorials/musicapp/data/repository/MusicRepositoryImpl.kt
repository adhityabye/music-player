package by.tutorials.musicapp.data.repository

import by.tutorials.musicapp.data.model.Song
import by.tutorials.musicapp.data.remote.MusicApiService

class MusicRepositoryImpl(
    private val api: MusicApiService
) : MusicRepository {
    override suspend fun search(term: String, limit: Int, offset: Int): List<Song> {
        return api.searchSongs(term, limit, offset).results
    }
}