package com.example.myshoppinglist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myshoppinglist.data.local.ShoppingItem
import com.example.myshoppinglist.remote.ImageResponse
import com.example.myshoppinglist.repo.ShoppingRepository
import com.example.myshoppinglist.util.Constants
import com.example.myshoppinglist.util.Event
import com.example.myshoppinglist.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepository
) : ViewModel() {

    val shoppingItems = repository.observeAllShoppingItems()
    val totalPrice = repository.observeTotalPrice()

    private val _imagePath = MutableLiveData<Event<Resource<ImageResponse>>>()
    val images: LiveData<Event<Resource<ImageResponse>>> = _imagePath

    private val _curImageUrl = MutableLiveData<String>()
    val curImageUrl: LiveData<String> = _curImageUrl

    private val _insertShoppingItemStatus = MutableLiveData<Event<Resource<ShoppingItem>>>()
    val insertShoppingItemStatus: LiveData<Event<Resource<ShoppingItem>>> =
        _insertShoppingItemStatus


    fun setCurImageUrl(url: String) {
        _curImageUrl.postValue(url)
    }


    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {
        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "The fields must not be empty",
                        null
                    )
                )
            )
            return
        }
        if (name.length > Constants.MAX_NAME_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "Name must not exceed ${Constants.MAX_NAME_LENGTH} characters",
                        null
                    )
                )
            )
            return
        }
        if (priceString.length > Constants.MAX_PRICE_LENGTH) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "Price must not exceed ${Constants.MAX_PRICE_LENGTH} characters",
                        null
                    )
                )
            )
            return
        }
        val amount = try {
            amountString.toInt()
        } catch (e: NumberFormatException) {
            _insertShoppingItemStatus.postValue(
                Event(
                    Resource.error(
                        "Please enter a valid amount",
                        null
                    )
                )
            )
            return
        }
        val shoppingItem =
            ShoppingItem(name, amount, priceString.toFloat(), _curImageUrl.value ?: "")
        insertShoppingItemToDb(shoppingItem)
        setCurImageUrl("")
        _insertShoppingItemStatus.postValue(Event(Resource.success(shoppingItem)))
    }

    private fun insertShoppingItemToDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }


    fun searchForImage(imageQuery: String) {
        if (imageQuery.isEmpty()) {
            return
        }
        _imagePath.value = (Event(Resource.loading(null)))
        viewModelScope.launch {
            val response = repository.searchForImage(imageQuery)
            _imagePath.postValue(Event(response))
        }
    }


}