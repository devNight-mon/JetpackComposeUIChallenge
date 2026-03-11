package com.devnight.jetpackcomposeuichallenge.data.remote

import com.devnight.jetpackcomposeuichallenge.data.model.ReelVideo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

/**
 * Created by Efe Şen on 06,03, 2026
 */
interface ReelsApiService {
    @GET("poudyalanil/ca84582cbeb4fc123a13290a586da925/raw")
    suspend fun getReels(): List<ReelVideo>
}

object RetrofitClient {
    private const val BASE_URL = "https://gist.githubusercontent.com/"

    val instance: ReelsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReelsApiService::class.java)
    }
}