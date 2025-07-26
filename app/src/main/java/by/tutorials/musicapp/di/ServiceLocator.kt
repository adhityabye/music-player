package by.tutorials.musicapp.di

import android.content.Context
import by.tutorials.musicapp.data.remote.MusicApiService
import by.tutorials.musicapp.data.repository.MusicRepository
import by.tutorials.musicapp.data.repository.MusicRepositoryImpl
import by.tutorials.musicapp.playback.PlaybackManager
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceLocator {
    private val apiService: MusicApiService by lazy {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()

        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(MusicApiService::class.java)
    }

    fun provideRepository(): MusicRepository =
        MusicRepositoryImpl(apiService)

    @Volatile private var playbackManager: PlaybackManager? = null
    fun providePlaybackManager(context: Context): PlaybackManager =
        playbackManager ?: synchronized(this) {
            playbackManager ?: PlaybackManager(context).also {
                playbackManager = it
            }
        }
}