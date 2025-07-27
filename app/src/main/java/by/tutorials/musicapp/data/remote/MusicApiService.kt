package by.tutorials.musicapp.data.remote

import by.tutorials.musicapp.data.model.SongResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MusicApiService {
    @GET("search")
    suspend fun searchSongs(
        @Query("term") term: String,
        @Query("limit") limit: Int = 8,
        @Query("offset")  offset: Int = 0
    ): SongResponse
}