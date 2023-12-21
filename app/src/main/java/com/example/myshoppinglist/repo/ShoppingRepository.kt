package com.example.myshoppinglist.repo

import androidx.lifecycle.LiveData
import com.example.myshoppinglist.data.local.ShoppingItem
import com.example.myshoppinglist.remote.ImageResponse
import com.example.myshoppinglist.util.Resource
import retrofit2.Response

interface ShoppingRepository {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>

    fun observeTotalPrice(): LiveData<Float>

    suspend fun searchForImage(imageQuery: String): Resource<ImageResponse>
}