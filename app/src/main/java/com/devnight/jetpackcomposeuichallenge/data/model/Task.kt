package com.devnight.jetpackcomposeuichallenge.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Efe Şen on 25,02,2026
 */
@Entity(tableName = "tasks")
data class Task (
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val title : String,
    val isCompleted: Boolean = false
)