package com.devnight.jetpackcomposeuichallenge.player

import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.devnight.jetpackcomposeuichallenge.manager.VideoCacheManager

object VideoPlayerPool {
    private var player: ExoPlayer? = null

    fun getPlayer(context: android.content.Context): ExoPlayer {
        if (player == null) {
            player = ExoPlayer.Builder(context).build()
            player?.repeatMode = Player.REPEAT_MODE_ONE
        }
        return player!!
    }
}

/**
 * Created by Efe Şen on 06,03, 2026
 */

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(videoUrl: String, isPlaying: Boolean) {
    val context = LocalContext.current

    val exoPlayer = remember {
        VideoPlayerPool.getPlayer(context)
    }

    LaunchedEffect(videoUrl) {
        val dataSourceFactory = VideoCacheManager.getCacheDataSourceFactory(context)
        val mediaItem = MediaItem.fromUri(videoUrl)
        val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(mediaItem)

        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()
    }

    // Manage playback status
    LaunchedEffect(isPlaying) {
        exoPlayer.playWhenReady = isPlaying
        if (isPlaying) exoPlayer.play() else exoPlayer.pause()
    }

    // Clearing the VideoPlayerView and releasing the player when navigating to another page
    DisposableEffect(Unit) {
        onDispose {
            // Pause when Composable is completely removed from the screen
            exoPlayer.pause()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT // Kararma olmaması için ZOOM
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { playerView ->
            if (playerView.player != exoPlayer) {
                playerView.player = exoPlayer
            }
        }
    )
}