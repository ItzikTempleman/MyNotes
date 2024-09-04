package com.itzik.mynotes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.itzik.mynotes.project.repositories.AppRepositoryInterface
import com.itzik.mynotes.project.viewmodels.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


class UserViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var mockRepo: AppRepositoryInterface
    private lateinit var mockUserViewModel: UserViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup(){
        Dispatchers.setMain(testDispatcher)
        mockUserViewModel= UserViewModel(mockRepo)
    }

    @After
    fun tearDown(){
        Dispatchers.resetMain()
        testDispatcher.cancel()
    }


    @Test
    fun `check if user is created properly`()= runTest {

    }
}