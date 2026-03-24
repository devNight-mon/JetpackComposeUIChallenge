package com.devnight.jetpackcomposeuichallenge.manager

import android.content.Context
import android.util.Log
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.okhttp.OkHttpDataSource
import okhttp3.Dns
import okhttp3.OkHttpClient
import java.io.File
import java.net.Inet4Address
import java.net.InetAddress
import java.util.concurrent.TimeUnit

@OptIn(UnstableApi::class)
object VideoCacheManager {
    private var cache: SimpleCache? = null
    private const val TAG = "VideoCacheManager"

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .dns(object : Dns {
                override fun lookup(hostname: String): List<InetAddress> {
                    val addresses = try {
                        Dns.SYSTEM.lookup(hostname)
                    } catch (e: Exception) {
                        emptyList()
                    }

                    val ipv4Addresses = addresses.filterIsInstance<Inet4Address>()
                    
                    if (ipv4Addresses.isNotEmpty()) {
                        return ipv4Addresses
                    }

                    // Workaround: If the specific hostname fails to give IPv4, 
                    // try resolving the generic storage hostname as a fallback.
                    if (hostname.contains("googleapis.com")) {
                        Log.w(TAG, "No IPv4 for $hostname, attempting cross-resolution for storage.googleapis.com")
                        try {
                            val altAddresses = Dns.SYSTEM.lookup("storage.googleapis.com")
                            val altIpv4 = altAddresses.filterIsInstance<Inet4Address>()
                            if (altIpv4.isNotEmpty()) {
                                Log.d(TAG, "Successfully found alternative IPv4 via storage.googleapis.com")
                                return altIpv4
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Alternative resolution failed", e)
                        }
                    }

                    Log.w(TAG, "Final fallback to IPv6 for $hostname")
                    return addresses.ifEmpty { Dns.SYSTEM.lookup(hostname) }
                }
            })
            .build()
    }

    fun getCache(context: Context): SimpleCache {
        if (cache == null) {
            val cacheDir = File(context.cacheDir, "media_cache")
            if (!cacheDir.exists()) cacheDir.mkdirs()
            cache = SimpleCache(
                cacheDir,
                LeastRecentlyUsedCacheEvictor(100 * 1024 * 1024),
                StandaloneDatabaseProvider(context)
            )
        }
        return cache!!
    }

    fun getCacheDataSourceFactory(context: Context): DataSource.Factory {
        val httpDataSourceFactory = OkHttpDataSource.Factory(okHttpClient)
            .setUserAgent(Util.getUserAgent(context, context.packageName))

        return CacheDataSource.Factory()
            .setCache(getCache(context))
            .setUpstreamDataSourceFactory(DefaultDataSource.Factory(context, httpDataSourceFactory))
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }
}