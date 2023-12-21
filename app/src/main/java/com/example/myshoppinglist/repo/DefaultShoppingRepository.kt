package com.example.myshoppinglist.repo

import androidx.lifecycle.LiveData
import com.example.myshoppinglist.data.local.ShoppingDao
import com.example.myshoppinglist.data.local.ShoppingItem
import com.example.myshoppinglist.remote.ImageResponse
import com.example.myshoppinglist.remote.PixelBayApi
import com.example.myshoppinglist.util.Resource
import retrofit2.Response
import javax.inject.Inject

class DefaultShoppingRepository @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val pixelBayApi: PixelBayApi
): ShoppingRepository{
    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
       return shoppingDao.observeAllShoppingItems()
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return shoppingDao.observeTotalPrice()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return try {
            val response = pixelBayApi.searchFroImage(imageQuery)
            if (response.isSuccessful){
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("An unknown error occured", null)
            }else {
                return Resource.error("Couldn't reach the server. Check your internet connection", null)
            }
        }
        catch (e: Exception){
            Resource.error("Couldn't reach the server. Check your internet connection", null)
        }
    }


}