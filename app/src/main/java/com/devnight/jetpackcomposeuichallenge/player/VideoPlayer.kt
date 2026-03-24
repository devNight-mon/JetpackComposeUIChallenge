package com.devnight.jetpackcomposeuichallenge.player

import android.util.Log
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.DefaultMediaSourceFactory
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.devnight.jetpackcomposeuichallenge.manager.VideoCacheManager

private const val TAG = "VideoPlayerDebug"

object VideoPlayerPool {
    private var player: ExoPlayer? = null

    @OptIn(UnstableApi::class)
    fun getPlayer(context: android.content.Context): ExoPlayer {
        if (player == null) {
            Log.d(TAG, "Initializing ExoPlayer...")
            
            // System properties as a baseline
            try {
                System.setProperty("java.net.preferIPv4Stack", "true")
                System.setProperty("java.net.preferIPv6Addresses", "false")
            } catch (e: Exception) {
                Log.e(TAG, "Error setting system properties: ${e.message}")
            }

            val dataSourceFactory = VideoCacheManager.getCacheDataSourceFactory(context)
            
            player = ExoPlayer.Builder(context.applicationContext)
                .setMediaSourceFactory(DefaultMediaSourceFactory(dataSourceFactory))
                .build().apply {
                    repeatMode = Player.REPEAT_MODE_ONE
                }
        }
        return player!!
    }
}

@OptIn(UnstableApi::class)
@Composable
fun VideoPlayer(videoUrl: String, isPlaying: Boolean) {
    val context = LocalContext.current
    val exoPlayer = remember { VideoPlayerPool.getPlayer(context) }

    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlayerError(error: PlaybackException) {
                Log.e(TAG, "Player Error [${error.errorCode}]: ${error.message}", error)
            }
        }
        exoPlayer.addListener(listener)
        onDispose {
            exoPlayer.removeListener(listener)
        }
    }

    LaunchedEffect(videoUrl) {
        if (videoUrl.isNotEmpty()) {
            // Rewrite URL to use storage.googleapis.com which often has better IPv4 resolution/availability
            val secureUrl = videoUrl.replace("http://", "https://")
                .replace("commondatastorage.googleapis.com", "storage.googleapis.com")

            Log.d(TAG, "Loading Video: $secureUrl")
            
            try {
                val mediaItem = MediaItem.fromUri(secureUrl)
                exoPlayer.stop()
                exoPlayer.clearMediaItems()
                exoPlayer.setMediaItem(mediaItem)
                exoPlayer.prepare()
                if (isPlaying) {
                    exoPlayer.play()
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error preparing MediaItem: ${e.message}")
            }
        }
    }

    LaunchedEffect(isPlaying) {
        if (isPlaying) {
            exoPlayer.play()
        } else {
            exoPlayer.pause()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                this.player = exoPlayer
            }
        },
        modifier = Modifier.fillMaxSize(),
        update = { view ->
            if (view.player != exoPlayer) {
                view.player = exoPlayer
            }
        },
        onRelease = { view ->
            view.player = null
        }
    )
}