package com.devnight.jetpackcomposeuichallenge.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devnight.jetpackcomposeuichallenge.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Created by Efe Şen on 26,02,2026
 */
@Dao
interface TaskDao {
    // Tüm görevleri getir (Flow kullanarak veriler değiştikçe UI otomatik güncellenecek)
    @Query("SELECT * FROM tasks ORDER BY id DESC")
    fun getAllTasks(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)


    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)
}