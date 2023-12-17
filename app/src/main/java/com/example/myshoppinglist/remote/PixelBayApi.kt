package com.example.myshoppinglist.remote

import com.example.myshoppinglist.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixelBayApi {

    @GET("/api/")
    suspend fun searchFroImage(
        @Query("q") searchQuery: String,
        @Query("key") apiKey: String = BuildConfig.API_KEY
    ): Response<ImageResponse>
}