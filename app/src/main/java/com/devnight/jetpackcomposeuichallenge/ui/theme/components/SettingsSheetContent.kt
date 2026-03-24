package com.devnight.jetpackcomposeuichallenge.ui.theme.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.devnight.jetpackcomposeuichallenge.R

/**
 * Created by Efe Şen on 27,02,2026
 */
@Composable
fun SettingsSheetContent(
    onDeleteAll: () -> Unit,
    onDismiss: () -> Unit,
    notificationsEnabled: Boolean,
    onToggleNotifications: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.92f)
            .background(Color(0xFFF2F2F7))
            .padding(16.dp)
            .navigationBarsPadding()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center // Genel hizalama merkez
        ) {

            IconButton(
                onClick = { onDismiss() },
                modifier = Modifier.align(Alignment.CenterStart) // Box içinde sola yasla
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Kapat",
                    tint = Color.Gray // Görseldeki gibi hafif gri tonu
                )
            }

            // 2. Başlık (Tam Merkezde)
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            ListItem(
                headlineContent = { Text("Efe Şen", fontWeight = FontWeight.Bold) },
                supportingContent = { Text("Account") },
                leadingContent = {
                    Surface(
                        modifier = Modifier
                            .size(52.dp)
                            .border(
                                width = 2.dp, color = Color.White, shape = RoundedCornerShape(12.dp)
                            ),
                        shape = RoundedCornerShape(12.dp),
                        color = Color.LightGray,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ddd),
                            contentDescription = "Profile Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(12.dp)), // Resmi çerçevenin içine hapseder
                            contentScale = ContentScale.Crop // Kare alanı tam doldurması için
                        )
                    }
                },
                trailingContent = {
                    Icon(
                        Icons.Default.KeyboardArrowRight, contentDescription = null
                    )
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )
        }

        Text(
            "Management",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp, top = 24.dp, bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column {
                ListItem(
                    headlineContent = { Text("Circle Members") }, leadingContent = {
                        Icon(
                            Icons.Default.AccountCircle, contentDescription = null
                        )
                    }, trailingContent = {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null
                        )
                    }, colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp
                )

                ListItem(
                    headlineContent = { Text("Manage Places") },
                    leadingContent = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                    trailingContent = {
                        Icon(
                            Icons.Default.KeyboardArrowRight, contentDescription = null
                        )
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }
        }

        Text(
            "Notifications",
            style = MaterialTheme.typography.labelMedium,
            color = Color.Gray,
            modifier = Modifier.padding(start = 8.dp, top = 24.dp, bottom = 8.dp)
        )


        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp)
        ) {
            ListItem(
                headlineContent = { Text("Push Notifications") },
                leadingContent = { Icon(Icons.Default.Notifications, contentDescription = null) },
                trailingContent = {
                    Switch(
                        checked = notificationsEnabled, onCheckedChange = onToggleNotifications
                    )
                },
                colors = ListItemDefaults.colors(containerColor = Color.Transparent)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onDeleteAll, modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(16.dp),
                    clip = false,
                    ambientColor = Color(0xFF356CF0).copy(alpha = 0.6f),
                    spotColor = Color(0xFF356CF0)
                ), shape = RoundedCornerShape(16.dp), colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF356CF0)
            ), elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp, pressedElevation = 4.dp
            )
        ) {
            Text("Delete all task", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}
