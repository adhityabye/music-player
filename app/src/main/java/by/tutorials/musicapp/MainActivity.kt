package by.tutorials.musicapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import kotlinx.coroutines.launch

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
                MainScreen(
                    viewModel = viewModel,
                    playbackManager = playbackManager
                )
            }
        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: SongViewModel,
    playbackManager: PlaybackManager
) {
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var query by remember { mutableStateOf("") }
    var playingId by remember { mutableStateOf<Long?>(null) }
    var isPlaying by remember { mutableStateOf(false) }
    var position by remember { mutableStateOf(0f) }
    var duration by remember { mutableStateOf(0f) }

    val songs by viewModel.songs.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val endReached  by viewModel.endReached.collectAsState()

    fun showMessage(msg: String) {
        scope.launch {
            snackbarHostState.showSnackbar(msg)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.searchSongs("rock")
    }

    Scaffold(
        modifier     = modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
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
                    .fillMaxWidth()
            ) {
                when {
                    loading && songs.isEmpty() ->
                        CircularProgressIndicator(Modifier.align(Alignment.Center))

                    error != null ->
                        Text("Error: $error", Modifier.align(Alignment.Center), color = MaterialTheme.colorScheme.error)

                    !loading && songs.isEmpty() ->
                        Text("Tidak ada hasil yang ditemukan", Modifier.align(Alignment.Center))

                    else -> MusicList(
                        songs         = songs,
                        playingId     = playingId,
                        isLoadingMore = loading,
                        endReached    = endReached,
                        onSongClick   = {
                            it.previewUrl?.let { url ->
                                playbackManager.play(url)
                                playingId = it.id
                                isPlaying = true
                            } ?: run {
                                showMessage("Tidak ada preview untuk ${it.title}")
                            }
                        },
                        onLoadMore    = { viewModel.loadMore() }
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
                val currentSong = songs.getOrNull(currentIndex) ?: return@let

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

}

