package com.example.myshoppinglist.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.myshoppinglist.repo.FakeShoppingRepository
import com.example.myshoppinglist.util.Constants
import com.example.myshoppinglist.util.Status
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class ShoppingViewModelTest {

    private lateinit var viewModel: ShoppingViewModel


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()


    @Before
    fun setUp() {
        viewModel = ShoppingViewModel(FakeShoppingRepository())
    }


    @Test
    fun `insert shopping item with empty field, returns error` () {
        viewModel.insertShoppingItem("name", "", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }


    @Test
    fun `insert shopping item with too long name, returns error` () {
        val name = buildString {
            for (i in 1..Constants.MAX_NAME_LENGTH + 1) {
                append(1)
            }
        }
        viewModel.insertShoppingItem(name, "5", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }


    @Test
    fun `insert shopping item with too long price, returns error` () {
        val price = buildString {
            for (i in 1..Constants.MAX_PRICE_LENGTH + 1) {
                append(1)
            }
        }
        viewModel.insertShoppingItem("name", "5", price)
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }


    @Test
    fun `insert shopping item with too high amount, returns error` () {
        viewModel.insertShoppingItem("name", "999999999999999999999", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.ERROR)
    }


    @Test
    fun `insert shopping item with valid input, returns success` () {
        viewModel.insertShoppingItem("name", "5", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
    @Test
    fun `insert shopping item without image, returns Success` () {
        viewModel.insertShoppingItem("name", "5", "3.0")
        val value = viewModel.insertShoppingItemStatus.getOrAwaitValueTest()
        assertThat(value.getContentIfNotHandled()?.status).isEqualTo(Status.SUCCESS)
    }
    @Test
    fun `curImageUrl Is Empty After Inserting Item` () {
        viewModel.setCurImageUrl("random")
        viewModel.insertShoppingItem("name", "5", "3.0")
        assertThat(viewModel.curImageUrl.getOrAwaitValueTest()).isEqualTo("")
    }

}