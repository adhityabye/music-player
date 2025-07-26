package by.tutorials.musicapp.data.model

import com.google.gson.annotations.SerializedName

data class Song(
    @SerializedName("trackId") val id: Long,
    @SerializedName("artistName") val artist: String,
    @SerializedName("trackName") val title: String,
    @SerializedName("previewUrl") val previewUrl: String?,
    @SerializedName("artworkUrl") val artworkUrl: String?,
    @SerializedName("trackTimeMillis") val durationMillis: Long
)