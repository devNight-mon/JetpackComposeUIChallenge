package com.devnight.jetpackcomposeuichallenge.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devnight.jetpackcomposeuichallenge.data.local.TaskRepository
import com.devnight.jetpackcomposeuichallenge.data.model.Task
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

/**
 * Created by Efe Şen on 25,02,2026
 */
class TodoViewModel(private val repository: TaskRepository) : ViewModel() {

    // Artık mutableStateListOf kullanmıyoruz, repository'deki Flow'u alıyoruz
    val taskList: Flow<List<Task>> = repository.allTasks


    // Yeni görev ekleme fonksiyonu
    fun addTask(title: String) {
        viewModelScope.launch {
            repository.insert(Task(title = title))
        }
    }


    // Görev silme fonksiyonu
    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }

    // Görevi tamamlandı/tamamlanmadı yapma
    fun toggleTaskCompleted(task: Task) {
      viewModelScope.launch {
          repository.update(task.copy(isCompleted = !task.isCompleted))
      }
    }

}