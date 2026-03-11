package com.devnight.jetpackcomposeuichallenge.data.model

/**
 * Created by Efe Şen on 06,03,2026
 */
data class ReelResponse (
    val videos: List<ReelVideo>
)

data class ReelVideo(
    val id: String,
    val title: String,
    val thumbnailUrl: String,
    val author: String,
    val videoUrl: String,
    val description: String,
    val subscriber: String,
    val isLive: Boolean
)