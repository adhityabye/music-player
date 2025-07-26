package by.tutorials.musicapp.data.repository

import by.tutorials.musicapp.data.model.Song
import by.tutorials.musicapp.data.remote.MusicApiService

class MusicRepositoryImpl(
    private val api: MusicApiService
) : MusicRepository {
    override suspend fun searchSongs(term: String): List<Song> =
        api.searchSongs(term).results
}