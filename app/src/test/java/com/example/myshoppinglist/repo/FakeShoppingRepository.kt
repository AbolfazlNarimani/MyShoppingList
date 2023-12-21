package com.example.myshoppinglist.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myshoppinglist.data.local.ShoppingItem
import com.example.myshoppinglist.remote.ImageResponse
import com.example.myshoppinglist.util.Resource

class FakeShoppingRepository : ShoppingRepository {
    private val shoppingItems = mutableListOf<ShoppingItem>()

    private val observableShoppingItems = MutableLiveData<List<ShoppingItem>>(shoppingItems)

    private val observableTotalPrice = MutableLiveData<Float>()

    private var shouldReturnNetworkError = false


    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    private fun refreshLiveData() {
        observableShoppingItems.postValue(shoppingItems)
        observableTotalPrice.postValue(getTotalPrice())
    }

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        this.shoppingItems.add(shoppingItem)
        refreshLiveData()
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        this.shoppingItems.remove(shoppingItem)
        refreshLiveData()
    }

    override fun observeAllShoppingItems(): LiveData<List<ShoppingItem>> {
        return observableShoppingItems
    }

    override fun observeTotalPrice(): LiveData<Float> {
        return observableTotalPrice
    }

    private fun getTotalPrice(): Float? {
        return shoppingItems.sumOf { it.price.toDouble() }.toFloat()
    }

    override suspend fun searchForImage(imageQuery: String): Resource<ImageResponse> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        }else {
            Resource.success(ImageResponse(emptyList(), 0, 0))
        }
    }
}