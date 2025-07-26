package by.tutorials.musicapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import by.tutorials.musicapp.ui.components.PlaybackControls
import by.tutorials.musicapp.ui.components.SearchBar
import by.tutorials.musicapp.ui.theme.MusicAppTheme
import by.tutorials.musicapp.di.ServiceLocator
import by.tutorials.musicapp.playback.PlaybackManager
import by.tutorials.musicapp.ui.components.MusicList
import by.tutorials.musicapp.viewmodel.SongViewModel
import by.tutorials.musicapp.viewmodel.SongViewModelFactory
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {

    private val playbackManager: PlaybackManager by lazy {
        ServiceLocator.providePlaybackManager(applicationContext)
    }
    private lateinit var viewModel: SongViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, SongViewModelFactory())
            .get(SongViewModel::class.java)

        setContent {
            MusicAppTheme {
                Scaffold { inner ->
                    MainScreen(
                        modifier = Modifier.padding(inner),
                        viewModel = viewModel,
                        playbackManager = playbackManager
                    )
                }
            }
        }
    }
}

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: SongViewModel,
    playbackManager: PlaybackManager
) {
    val context = LocalContext.current

    var query by remember { mutableStateOf("") }
    var playingId by remember { mutableStateOf<Long?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var position by remember { mutableStateOf(0f) }
    var duration by remember { mutableStateOf(0f) }

    val songs by viewModel.songs.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()



    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        SearchBar(
            text = query,
            onTextChange = { query = it },
            onSearch = { viewModel.searchSongs(query) }
        )

        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth()) {
            when {
                loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                error != null -> Text(
                    text = "error: $error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )

                else -> MusicList(
                    songs = songs,
                    playingId = playingId,
                    onSongClick = { song ->
                        song.previewUrl?.let { url ->
                            playbackManager.play(url)
                        }
                        playingId = song.id
                        isPlaying = true
                        position = 0f
                        duration = playbackManager.duration().toFloat()
                    }
                )
            }
        }
        LaunchedEffect(key1 = isPlaying, key2 = playingId) {
            if (isPlaying && playingId != null) {

                while (duration == 0f) {
                    duration = playbackManager.duration().toFloat()
                    delay(100)
                }

                while (isPlaying) {
                    position = playbackManager.currentPosition().toFloat()
                    delay(300)
                }
            }
        }

        playingId?.let { currentId ->
            val currentIndex = songs.indexOfFirst { it.id == currentId }
            val currentSong = songs.getOrNull(currentIndex) ?: return

            PlaybackControls(
                title = currentSong.title,
                artist = currentSong.artist,
                isPlaying = isPlaying,
                position = position,
                duration = duration,

                onPrevious = {
                    if (currentIndex > 0) {
                        songs[currentIndex - 1].let { prev ->
                            prev.previewUrl?.let { playbackManager.play(it) }
                            playingId = prev.id
                            isPlaying = true
                        }
                    } else {
                        Toast
                            .makeText(context, "At first list", Toast.LENGTH_SHORT)
                            .show()
                    }
                },

                onPlayPause = {
                    if (isPlaying) playbackManager.pause()
                    else playbackManager.resume()
                    isPlaying = !isPlaying
                },

                onNext = {
                    if (currentIndex < songs.lastIndex) {
                        songs[currentIndex + 1].let { next ->
                            next.previewUrl?.let { playbackManager.play(it) }
                            playingId = next.id
                            isPlaying = true
                        }
                    } else {
                        Toast
                            .makeText(context, "At end list", Toast.LENGTH_SHORT)
                            .show()
                    }
                },

                onSeek = { newPos ->
                    playbackManager.seekTo(newPos.toInt())
                    position = newPos
                }
            )
        }
    }
}

