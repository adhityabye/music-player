package by.tutorials.musicapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import by.tutorials.musicapp.R

@Composable
fun PlaybackControls(
    title: String,
    artist: String,
    isPlaying: Boolean,
    position: Float,
    duration: Float,
    onPrevious: () -> Unit,
    onPlayPause: () -> Unit,
    onNext: () -> Unit,
    onSeek: (Float) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "$title - $artist",
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = onPrevious) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_previous),
                    contentDescription = "Previous"
                )
            }
            IconButton(onClick = onPlayPause) {
                Icon(
                    painter = painterResource(
                        id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                    ),
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(onClick = onNext) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_next),
                    contentDescription = "Previous"
                )
            }
        }

        Slider(
            value = position,
            onValueChange = onSeek,
            valueRange = 0f..duration,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            colors = SliderDefaults.colors(
                thumbColor = Color.Black,
                activeTrackColor = Color.Black,
                inactiveTrackColor = Color.LightGray,
                activeTickColor = Color.Black,
                inactiveTickColor = Color.LightGray
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PlaybackControlsPreview() {
    PlaybackControls(
        title      = "33x",
        artist     = "Perunggu",
        isPlaying  = true,
        position   = 120_000f,
        duration   = 300_000f,
        onPrevious = {},
        onPlayPause= {},
        onNext     = {},
        onSeek     = {}
    )
}