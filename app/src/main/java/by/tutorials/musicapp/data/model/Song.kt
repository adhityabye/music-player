package by.tutorials.musicapp.data.model

data class Song(
    val id: Long,
    val title: String,
    val artist: String,
    val artworkUrl: String,
    val previewUrl: String = "",
    val durationMs: Long = 0L
)