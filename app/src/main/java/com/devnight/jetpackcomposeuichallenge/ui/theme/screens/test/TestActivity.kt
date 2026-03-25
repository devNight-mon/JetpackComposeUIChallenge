package com.devnight.jetpackcomposeuichallenge.ui.theme.screens.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateBounds
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ModifierExample()
        }
    }

    @Composable
    fun MyFirstComposeApp() {
        var message by remember { mutableStateOf("Merhaba Compose") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = message, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { message = "Button clicked" }) {
                Text("Tıkla")
            }
        }
    }

    @Composable
    fun LayoutExample() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Solda")
                    Text("Sağda")
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Blue)
                )
            }
        }
    }

    @Composable
    fun CounterApp() {
        var count by remember { mutableIntStateOf(0) }

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Sayaç: $count", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { count++ }) { Text("+") }
                Button(onClick = { count-- }) { Text("-") }
            }
        }
    }

    @Composable
    fun LazyListExample() {
        val items = List(20) { "Öğe $it" }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items) { item ->
                Text(
                    text = item,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.Cyan)
                        .padding(8.dp)
                )
            }
        }
    }

    @Composable
    fun TextFieldExample() {
        var name by remember { mutableStateOf("") }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Adınızı girin") }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Merhaba $name")
        }
    }

    @Composable
    fun ModifierExample() {
        Box(
            modifier = Modifier
                .size(250.dp)
                .background(Color.Magenta)
                .border(2.dp, Color.Black)
                .padding(16.dp),
            contentAlignment = Alignment.Center

        ) {
            Text("Merhaba!", color = Color.White)
        }
    }


}