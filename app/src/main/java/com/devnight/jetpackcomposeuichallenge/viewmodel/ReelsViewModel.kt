package com.devnight.jetpackcomposeuichallenge.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devnight.jetpackcomposeuichallenge.data.model.ReelVideo
import com.devnight.jetpackcomposeuichallenge.data.remote.RetrofitClient
import kotlinx.coroutines.launch

/**
 * Created by Efe Şen on 06,03, 2026
 */
class ReelsViewModel : ViewModel() {
    var reelsList by mutableStateOf<List<ReelVideo>>(emptyList())
    var isLoading by mutableStateOf(false)

    init {
        fetchReels()
    }

    private fun fetchReels() {
        viewModelScope.launch {
            isLoading = true
            try {
                reelsList = RetrofitClient.instance.getReels()
            }catch (e: Exception) {
                Log.e("ReelsError", "Veri çekilemedi: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

}