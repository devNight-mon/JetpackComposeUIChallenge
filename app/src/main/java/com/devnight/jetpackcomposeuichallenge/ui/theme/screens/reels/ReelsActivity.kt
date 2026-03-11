package com.devnight.jetpackcomposeuichallenge.ui.theme.screens.reels

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devnight.jetpackcomposeuichallenge.data.model.ReelVideo
import com.devnight.jetpackcomposeuichallenge.player.VideoPlayer
import com.devnight.jetpackcomposeuichallenge.viewmodel.ReelsViewModel
import kotlinx.coroutines.launch

class ReelsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            InstagramReelsChallengeStep1()
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun InstagramReelsChallengeStep1(viewModel: ReelsViewModel = viewModel()) {
        if (viewModel.reelsList.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color.White)
            }
            return
        }

        val pagerState = rememberPagerState(pageCount = { viewModel.reelsList.size })
        val sheetState = rememberStandardBottomSheetState(initialValue = SheetValue.Hidden, skipHiddenState = false)
        val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)
        val coroutineScope = rememberCoroutineScope()

        BoxWithConstraints(modifier = Modifier.fillMaxSize().background(Color.Black)) {
            val fullHeightPx = constraints.maxHeight.toFloat()
            val currentOffset by remember { derivedStateOf { try { sheetState.requireOffset() } catch (e: Exception) { fullHeightPx } } }
            // Progress goes from 0 to 0.5 (since sheet is 50% height)
            val progress by remember { derivedStateOf { (1f - (currentOffset / fullHeightPx)).coerceIn(0f, 1f) } }

            BottomSheetScaffold(
                scaffoldState = scaffoldState,
                sheetPeekHeight = 0.dp,
                sheetContainerColor = Color(0xFF121212),
                sheetDragHandle = {
                    Box(Modifier.padding(vertical = 10.dp).size(width = 40.dp, height = 4.dp).background(Color.White.copy(0.2f), RoundedCornerShape(2.dp)))
                },
                sheetContent = {
                    Box(modifier = Modifier.fillMaxWidth().fillMaxHeight(0.50f)) {
                        ReelsCommentSheetContent()
                    }
                }
            ) {
                ReelsVideoContainer(
                    progress = progress,
                    fullHeightPx = fullHeightPx,
                    reelsList = viewModel.reelsList,
                    pagerState = pagerState,
                    onCommentClick = { coroutineScope.launch { sheetState.expand() } }
                )
            }
        }
    }

    @Composable
    fun ReelsVideoContainer(
        progress: Float,
        fullHeightPx: Float,
        reelsList: List<ReelVideo>,
        pagerState: PagerState,
        onCommentClick: () -> Unit
    ) {
        val density = LocalDensity.current
        val gapPx = with(density) { 20.dp.toPx() }
        
        // Normalize progress to 0..1 for the 50% sheet expansion
        val normalizedProgress = (progress * 2f).coerceIn(0f, 1f)
        
        // Target scale should leave a 20dp gap above the 50% height sheet
        // Final height = 0.5 * fullHeight - 20dp
        // liveScale = Final height / fullHeight = 0.5 - (20dp / fullHeight)
        val targetScale = 0.5f - (gapPx / fullHeightPx)
        val liveScale = 1f - (normalizedProgress * (1f - targetScale))
        
        val liveCornerRadius = (normalizedProgress * 32f).dp

        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black),
            contentAlignment = Alignment.TopCenter
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = liveScale
                        scaleY = liveScale
                        transformOrigin = TransformOrigin(0.5f, 0f)
                    }
                    .clip(RoundedCornerShape(liveCornerRadius))
                    .background(Color.DarkGray) // Placeholder color
            ) {
                VerticalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize(),
                    key = { reelsList[it].id },
                    beyondViewportPageCount = 0
                ) { _ -> Box(modifier = Modifier.fillMaxSize()) }

                val currentVideo = reelsList.getOrNull(pagerState.currentPage)
                currentVideo?.let { video ->
                    VideoPlayer(videoUrl = video.videoUrl, isPlaying = true)
                }

                Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = normalizedProgress * 0.4f)))

                ReelsUIOverlay(
                    progress = normalizedProgress,
                    userName = currentVideo?.author ?: "",
                    caption = currentVideo?.description ?: "",
                    onCommentClick = onCommentClick
                )
            }
        }
    }

    @Composable
    fun ReelsUIOverlay(
        progress: Float, userName: String,
        caption: String, onCommentClick: () -> Unit
    ) {
        val alphaOverlay = (1f - progress * 2f).coerceIn(0f, 1f)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer { alpha = alphaOverlay }
                .padding(top = 48.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
        ) {
            ReelsTopBar()
            ReelsRightActions(onCommentClick = onCommentClick)
            ReelsBottomInfo(userName, caption, modifier = Modifier.align(Alignment.BottomStart))
        }
    }

    @Composable
    fun ReelsTopBar() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(28.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("For you", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White)
                Spacer(Modifier.width(12.dp))
                Text("Friends", color = Color.White.copy(alpha = 0.7f), fontSize = 18.sp)
            }
            Icon(Icons.Default.Tune, null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
    }

    @Composable
    fun BoxScope.ReelsRightActions(onCommentClick: () -> Unit) {
        Column(
            modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ReelActionItem(Icons.Default.FavoriteBorder, "6,200")
            IconButton(onClick = onCommentClick) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Outlined.ChatBubbleOutline, null, tint = Color.White, modifier = Modifier.size(30.dp))
                    Text("50", color = Color.White, fontSize = 12.sp)
                }
            }
            ReelActionItem(Icons.Default.Share, "891")
            ReelActionItem(Icons.Default.BookmarkBorder, "431")
            Box(Modifier.size(28.dp).border(2.dp, Color.White, RoundedCornerShape(4.dp)).background(Color.DarkGray))
        }
    }

    @Composable
    fun ReelsBottomInfo(userName: String, caption: String, modifier: Modifier = Modifier) {
        Column(
            modifier = modifier.padding(bottom = 8.dp, end = 70.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(32.dp).clip(CircleShape).background(Color.Gray).border(1.dp, Color.White, CircleShape))
                Spacer(Modifier.width(8.dp))
                Text(userName, color = Color.White, fontWeight = FontWeight.Bold)
                Spacer(Modifier.width(10.dp))
                Surface(color = Color.Transparent, border = BorderStroke(1.dp, Color.White), shape = RoundedCornerShape(8.dp)) {
                    Text("Follow", color = Color.White, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp), fontSize = 12.sp)
                }
            }
            Text(caption, color = Color.White, fontSize = 14.sp)
        }
    }

    @Composable
    fun ReelActionItem(icon: ImageVector, label: String) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            Icon(icon, null, tint = Color.White, modifier = Modifier.size(30.dp))
            Text(label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        }
    }

    @Composable
    fun ReelsCommentSheetContent() {
        Column(modifier = Modifier.fillMaxSize().background(Color(0xFF121212))) {
            CommentHeaderSection()
            Box(modifier = Modifier.weight(1f)) { CommentListSection() }
            CommentInputSection()
        }
    }

    @Composable
    fun CommentHeaderSection() {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "For you", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White, modifier = Modifier.size(20.dp))
        }
    }

    @Composable
    fun CommentListSection() {
        val dummyComments = listOf(
            Triple("blue_lion_12", "That travel deserved that block. 🏀", "2d"),
            Triple("edubueno2022", "NBA ou NFL? Tá difícil diferenciar", "2d"),
            Triple("mp.wackkemm", "My nk Alexander walker wanna b Shai", "1d"),
            Triple("r.consult3", "Esse é bom", "1d")
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(dummyComments) { (user, comment, time) ->
                CommentItem(user, comment, time)
            }
        }
    }

    @Composable
    fun CommentItem(user: String, comment: String, time: String) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), verticalAlignment = Alignment.Top) {
            Box(Modifier.size(36.dp).clip(CircleShape).background(Color.Gray))
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(user, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(Modifier.width(8.dp))
                    Text(time, color = Color.Gray, fontSize = 12.sp)
                }
                Text(comment, color = Color.White, fontSize = 14.sp, modifier = Modifier.padding(top = 2.dp))
                Text("Reply", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 8.dp))
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Default.FavoriteBorder, null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                Text("67", color = Color.Gray, fontSize = 11.sp)
            }
        }
    }

    @Composable
    fun CommentInputSection() {
        Column(modifier = Modifier.fillMaxWidth().background(Color(0xFF121212)).padding(bottom = 20.dp)) {
            Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                listOf("❤️", "🙌", "🔥", "👏", "😢", "😍", "😮", "😂").forEach { emoji -> Text(emoji, fontSize = 24.sp) }
            }
            Row(modifier = Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(36.dp).clip(CircleShape).background(Color.DarkGray))
                Spacer(Modifier.width(12.dp))
                Surface(modifier = Modifier.weight(1f).height(42.dp), color = Color(0xFF262626), shape = RoundedCornerShape(21.dp), border = BorderStroke(1.dp, Color(0xFF363636))) {
                    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(horizontal = 16.dp)) {
                        Text("Join the conversation...", color = Color.Gray, fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
