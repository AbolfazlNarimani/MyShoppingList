package com.example.myshoppinglist.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ShoppingDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)
    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)
    @Update
    suspend fun updateShoppingItem(shoppingItem: ShoppingItem)

    @Query("SELECT * FROM `shopping-items`")
    fun observeAllShoppingItems(): LiveData<List<ShoppingItem>>
    @Query("SELECT SUM(price * amount) FROM `shopping-items`")
    fun observeTotalPrice(): LiveData<Float>
}