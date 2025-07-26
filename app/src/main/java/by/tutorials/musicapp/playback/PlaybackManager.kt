package by.tutorials.musicapp.playback

import android.content.Context
import android.media.MediaPlayer

class PlaybackManager(private val context: Context) {
    private var player: MediaPlayer? = null

    fun play(url: String) {
        if (player == null) {
            player = MediaPlayer().apply {
                setOnCompletionListener { /* TODO AUTO DISINI NEXT */ }
            }
        } else {
            player!!.reset()
        }
        player!!.setDataSource(url)
        player!!.prepareAsync()
        player!!.setOnPreparedListener { it.start() }
    }

    fun pause()  { player?.pause()  }
    fun resume() { player?.start()  }
    fun currentPosition(): Int = player?.currentPosition ?: 0
    fun duration(): Int = player?.duration ?: 0
    fun seekTo(positionMs: Int) {
        player?.seekTo(positionMs)
    }
}