package by.tutorials.musicapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import by.tutorials.musicapp.data.model.Song
import coil.compose.AsyncImage

@Composable
fun MusicList(
    modifier: Modifier = Modifier,
    songs: List<Song>,
    playingId: Long?,
    onSongClick: (Song) -> Unit
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {
        items(songs, key = { it.id }) { song ->
            SongCard(
                song      = song,
                isPlaying = (song.id == playingId),
                onClick   = { onSongClick(song) }
            )
        }
    }
}

@Composable
private fun SongCard(
    song: Song,
    isPlaying: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = song.artworkUrl,
                contentDescription = "${song.title} artwork",
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(4.dp))
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = song.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = song.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 12.sp
                )
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun MusicListScreenPreview() {
//    MusicList(
//        songs = listOf(
//            Song(
//                id = 1L,
//                artist = "33x",
//                title = "Perunggu",
//                previewUrl = "https://via.placeholder.com/300.mp3",
//                artworkUrl = "https://via.placeholder.com/100",
//                durationMillis = 180_000L
//            ),
//            Song(
//                id = 2L,
//                artist = "Elastic Heart",
//                title = "Reality Club",
//                previewUrl = "https://via.placeholder.com/300.mp3",
//                artworkUrl = "https://via.placeholder.com/100",
//                durationMillis = 200_000L
//            ),
//            Song(
//                id = 3L,
//                artist = "2112",
//                title = "Rush",
//                previewUrl = "https://via.placeholder.com/300.mp3",
//                artworkUrl = "https://via.placeholder.com/100",
//                durationMillis = 210_000L
//            ),
//            Song(
//                id = 4L,
//                artist = "Move Along",
//                title = "Summerlane",
//                previewUrl = "https://via.placeholder.com/300.mp3",
//                artworkUrl = "https://via.placeholder.com/100",
//                durationMillis = 190_000L
//            )
//        ),
//        playingId = 2L,
//        onSongClick = {}
//    )
//}
