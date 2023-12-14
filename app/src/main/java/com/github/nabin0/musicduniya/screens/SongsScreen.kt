package com.github.nabin0.musicduniya.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.nabin0.audioplayer.models.Audio
import com.github.nabin0.musicduniya.composables.AudioMiniPlayer
import com.github.nabin0.musicduniya.utils.TimeUtil
import com.github.nabin0.musicduniya.viewmodels.AudioPlayerProvider
import com.github.nabin0.musicduniya.viewmodels.SongsListViewModel

@Composable
fun SongsScreen(
    songsListViewModel: SongsListViewModel = hiltViewModel(),
    navigateToFullPlayerScreen: (Int) -> Unit,
) {

    val audioList = songsListViewModel.audioList

    Scaffold(
        bottomBar = {

        },
    ) {
        SongsListComposable(
            audioList = audioList,
            navigateToFullPlayerScreen = navigateToFullPlayerScreen,
            modifier = Modifier.padding(it.calculateBottomPadding())
        )
    }
}

@Composable
fun SongsListComposable(audioList: List<Audio>, navigateToFullPlayerScreen: (Int) -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val currentPlayingAudioIndex by AudioPlayerProvider.getAudioPlayer(context).currentPlayingMediaIndex.collectAsState()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item { Spacer(modifier = Modifier.statusBarsPadding()) }

        itemsIndexed(items = audioList) { index,audio ->
            val isSelected = (index == currentPlayingAudioIndex)
            AudioItem(audio = audio, onItemClick = {
                navigateToFullPlayerScreen(index)
            }, isSelected = isSelected)
        }
    }
}


@Composable
fun AudioItem(audio: Audio, onItemClick: () -> Unit, isSelected: Boolean) {
    val itemColor = if(isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else MaterialTheme.colorScheme.surface
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxWidth()
            .background(itemColor)
            .clickable {
                onItemClick.invoke()
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        audio.albumArtUrl?.let {
            CustomArtImage(
                albumArtUrl = it,
                modifier = Modifier.size(100.dp)
            )
        }
        Spacer(modifier = Modifier.width(2.dp))
        Column(
            modifier = Modifier.padding(start = 4.dp), verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = audio.title ?: "Unknown",
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(end = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = audio.artist ?: "Unknown",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = 16.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = TimeUtil.formatTime(audio.duration?.toLong() ?: 0L),
                    style = TextStyle(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Rounded.PlayArrow, contentDescription = "",
                    modifier = Modifier.padding(end = 16.dp),
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
fun CustomArtImage(albumArtUrl: String, modifier: Modifier = Modifier) {
    AsyncImage(
        modifier = modifier
            .padding(16.dp)
            .aspectRatio(1f)
            .heightIn(min = 220.dp, max = 240.dp),
        model = ImageRequest.Builder(LocalContext.current).data(albumArtUrl)
            .error(com.github.nabin0.audioplayer.R.drawable.audio_placeholder).crossfade(true)
            .build(),
        placeholder = painterResource(id = com.github.nabin0.audioplayer.R.drawable.audio_placeholder),
        contentDescription = "Albumart",
        contentScale = ContentScale.Crop
    )
}
