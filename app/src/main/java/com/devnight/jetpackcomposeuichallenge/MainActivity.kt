package com.devnight.jetpackcomposeuichallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devnight.jetpackcomposeuichallenge.data.local.AppDatabase
import com.devnight.jetpackcomposeuichallenge.data.local.TaskRepository
import com.devnight.jetpackcomposeuichallenge.data.model.Task
import com.devnight.jetpackcomposeuichallenge.ui.theme.TodoComposeAppTheme
import com.devnight.jetpackcomposeuichallenge.ui.theme.components.SettingsSheetContent
import com.devnight.jetpackcomposeuichallenge.viewmodel.TodoViewModel
import com.devnight.jetpackcomposeuichallenge.viewmodel.TodoViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database by lazy { AppDatabase.getDatabase(this) }
        val repository by lazy { TaskRepository(database.taskDao()) }

        setContent {
            TodoComposeAppTheme {
                val viewModel: TodoViewModel = viewModel(
                    factory = TodoViewModelFactory(repository)
                )
                TodoScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoScreen(viewModel: TodoViewModel) {
    val taskList by viewModel.taskList.collectAsState(initial = emptyList())
    var taskText by remember { mutableStateOf("") }

    var showSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    var showSettingsSheet by remember { mutableStateOf(false) } // Ayarlar için
    var notificationsEnabled by remember { mutableStateOf(true) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(), floatingActionButton = {
            Column(horizontalAlignment = Alignment.End) {
                SmallFloatingActionButton(
                    onClick = { showSettingsSheet = true },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ) {
                    Icon(Icons.Default.Settings, contentDescription = "Settings")
                }
                Spacer(modifier = Modifier.height(16.dp))

                FloatingActionButton(
                    onClick = { showSheet = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Ekle")
                }
            }
        }

    ) { innerPadding ->

        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {

            // --- LİSTE ALANI ---
            if (taskList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Henüz bir görev yok ✨",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(items = taskList, key = { it.id }) { task ->
                        TaskItem(
                            task = task,
                            onDelete = { viewModel.deleteTask(task) },
                            onStatusChange = { viewModel.toggleTaskCompleted(task) })
                    }
                }
            }
        }
        val scope = rememberCoroutineScope()
        val settingsSheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = true
        )

        if (showSettingsSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSettingsSheet = false },
                dragHandle = { BottomSheetDefaults.DragHandle() },
                sheetState = settingsSheetState,
                containerColor = Color(0xFFF2F2F7)
            ) {
                SettingsSheetContent(
                    notificationsEnabled = notificationsEnabled,
                    onToggleNotifications = { notificationsEnabled = it },
                    onDismiss = {
                        scope.launch {
                            settingsSheetState.hide()
                        }.invokeOnCompletion {
                            if (!settingsSheetState.isVisible) {
                                showSettingsSheet = false
                            }
                        }
                    },
                    onDeleteAll = {
                        showSettingsSheet = false
                    })
            }
        }

        // BottomSheet kısmı
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false }, sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .navigationBarsPadding()
                ) {
                    Text("Yeni Görev", fontWeight = FontWeight.Bold)
                    OutlinedTextField(
                        value = taskText,
                        onValueChange = { taskText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        singleLine = true,
                        placeholder = { Text("Ne yapacaksın") },
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                if (taskText.isNotBlank()) {
                                    viewModel.addTask(taskText)
                                    taskText = ""
                                    showSheet = false
                                }
                            }))

                    Button(
                        onClick = {
                            if (taskText.isNotBlank()) {
                                viewModel.addTask(taskText)
                                taskText = ""
                                showSheet = false
                            }
                        }, modifier = Modifier.fillMaxWidth()

                    ) { Text("Kaydet") }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(task: Task, onDelete: () -> Unit, onStatusChange: () -> Unit) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else false
        })

    SwipeToDismissBox(
        state = dismissState, enableDismissFromStartToEnd = false, backgroundContent = {
            val color =
                if (dismissState.dismissDirection == SwipeToDismissBoxValue.EndToStart) Color.Red.copy(
                    alpha = 0.7f
                ) else Color.Transparent

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, shape = RoundedCornerShape(12.dp))
                    .padding(horizontal = 20.dp), contentAlignment = Alignment.CenterEnd
            ) {
                Icon(Icons.Default.Delete, "Sil", tint = Color.White)
            }
        }) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = task.isCompleted, onCheckedChange = { onStatusChange() })
                Text(
                    text = task.title,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    style = if (task.isCompleted)
                        TextStyle(textDecoration = TextDecoration.LineThrough, color = Color.Gray)
                    else TextStyle.Default
                )
            }
        }
    }
}
