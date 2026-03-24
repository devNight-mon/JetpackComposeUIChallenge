package com.devnight.jetpackcomposeuichallenge.data.local

import com.devnight.jetpackcomposeuichallenge.data.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Created by Efe Şen on 26,02,2026
 */
class TaskRepository(private val taskDao: TaskDao) {
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun insert(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun update(task: Task) {
        taskDao.updateTask(task)
    }

    suspend fun delete(task: Task) {
        taskDao.deleteTask(task)
    }

}