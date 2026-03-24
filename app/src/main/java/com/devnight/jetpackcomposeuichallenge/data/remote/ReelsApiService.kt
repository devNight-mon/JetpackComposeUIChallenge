package com.devnight.jetpackcomposeuichallenge.data.remote

import com.devnight.jetpackcomposeuichallenge.data.model.ReelVideo
import com.devnight.jetpackcomposeuichallenge.manager.VideoCacheManager
import okhttp3.Dns
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.net.Inet4Address
import java.net.InetAddress
import java.util.concurrent.TimeUnit

/**
 * Created by Efe Şen on 06,03, 2026
 */
interface ReelsApiService {
    @GET("poudyalanil/ca84582cbeb4fc123a13290a586da925/raw")
    suspend fun getReels(): List<ReelVideo>
}

object RetrofitClient {
    private const val BASE_URL = "https://gist.githubusercontent.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .dns(object : Dns {
            override fun lookup(hostname: String): List<InetAddress> {
                val addresses = Dns.SYSTEM.lookup(hostname)
                val ipv4Addresses = addresses.filterIsInstance<Inet4Address>()
                return ipv4Addresses.ifEmpty { addresses }
            }
        })
        .build()

    val instance: ReelsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReelsApiService::class.java)
    }
}