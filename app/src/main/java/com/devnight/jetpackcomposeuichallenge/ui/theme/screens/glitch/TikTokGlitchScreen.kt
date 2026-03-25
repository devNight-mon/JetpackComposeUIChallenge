package com.devnight.jetpackcomposeuichallenge.ui.theme.screens.glitch

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun TikTokGlitchScreen() {
    var isGlitch by remember { mutableStateOf(false) }

    val symbol = """
          ⠀⠀⠀⠀⠀⠀⠀⠀⣶⣶⣶⡀⠀⠀
          ⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣷⣄⣀
          ⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣿⣿⣿
          ⠀⠀⢀⣠⣴⣶⣶⠀⣿⣿⣿⠉⠉⠉
          ⠀⣴⣿⣿⣿⣿⣿⠀⣿⣿⣿⠀⠀⠀
          ⢸⣿⣿⡿⠉⠀⠈⠀⣿⣿⣿⠀⠀⠀
          ⢹⣿⣿⣷⡀⠀⠀⣰⣿⣿⣿⠀⠀⠀
          ⠈⢿⣿⣿⣿⣿⣿⣿⣿⣿⠏⠀⠀⠀
          ⠀⠀⠙⠻⠿⠿⠿⠿⠋⠁⠀⠀⠀⠀
    """.trimIndent()

    val text = "TikTok"
    val offsetRange = -10..10

    LaunchedEffect(Unit) {
        while (true) {
            delay(100) // every 0.1s
            val randomDelay = Random.nextLong(0, 100)
            delay(randomDelay)
            isGlitch = !isGlitch
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Pink layer
        GlitchLayer(
            symbol = symbol,
            text = text,
            color = Color(0xFFFF0050),
            isGlitch = isGlitch,
            offsetRange = offsetRange
        )

        // Cyan layer
        GlitchLayer(
            symbol = symbol,
            text = text,
            color = Color(0xFF00F2EA),
            isGlitch = isGlitch,
            offsetRange = offsetRange
        )

        // Main layer (White)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = symbol,
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 20.sp
            )
            Text(
                text = text,
                color = Color.White,
                fontSize = 80.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun GlitchLayer(
    symbol: String,
    text: String,
    color: Color,
    isGlitch: Boolean,
    offsetRange: IntRange
) {
    val offsetX = if (isGlitch) Random.nextInt(offsetRange.first, offsetRange.last + 1).dp else 0.dp
    val offsetY = if (isGlitch) Random.nextInt(offsetRange.first, offsetRange.last + 1).dp else 0.dp

    Column(
        modifier = Modifier.offset(x = offsetX, y = offsetY),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(
            text = symbol,
            color = color,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 20.sp
        )
        Text(
            text = text,
            color = color,
            fontSize = 80.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview
@Composable
fun TikTokGlitchScreenPreview() {
    TikTokGlitchScreen()
}
