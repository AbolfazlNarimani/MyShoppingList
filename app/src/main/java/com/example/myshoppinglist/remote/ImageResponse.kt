package com.example.myshoppinglist.remote

data class ImageResponse(
    val imageResults: List<ImageResult>,
    val total: Int,
    val totalHits: Int
)