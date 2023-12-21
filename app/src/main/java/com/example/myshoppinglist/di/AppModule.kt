package com.example.myshoppinglist.di

import android.content.Context
import androidx.room.Room
import com.example.myshoppinglist.data.local.ShoppingDao
import com.example.myshoppinglist.data.local.ShoppingItemDatabase
import com.example.myshoppinglist.remote.PixelBayApi
import com.example.myshoppinglist.repo.ShoppingRepository
import com.example.myshoppinglist.util.Constants.BASE_URL
import com.example.myshoppinglist.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideShoppingItemDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(context, ShoppingItemDatabase::class.java, DATABASE_NAME).build()


    @Singleton
    @Provides
    fun provideShoppingDao(database: ShoppingItemDatabase) = database.shoppingDao()

    @Singleton
    @Provides
    fun providePixabayApi() : PixelBayApi{
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(PixelBayApi::class.java)
    }

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(
        dao: ShoppingDao,
        api: PixelBayApi
    ): ShoppingRepository = provideDefaultShoppingRepository(dao, api) as ShoppingRepository


}