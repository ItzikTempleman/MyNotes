package com.itzik.mynotes

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.repositories.AppRepositoryInterface
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule


class NoteViewModelTest {


        @get:Rule
        val instantTaskExecutorRule = InstantTaskExecutorRule()

        @get:Rule
        val mockitoRule: MockitoRule = MockitoJUnit.rule()

        @Mock
        private lateinit var mockRepository: AppRepositoryInterface
        private lateinit var mockNoteViewModel: NoteViewModel
        private val testDispatcher = StandardTestDispatcher()

        @Before
        fun setup() {
            Dispatchers.setMain(testDispatcher)
            mockNoteViewModel = NoteViewModel(mockRepository)
        }

        @After
        fun tearDown() {
            Dispatchers.resetMain()
            testDispatcher.cancel()
        }

        @Test
        fun `fetchNotesForUser get correct note `() = runTest {

            val userId = "10000001"
            val sampleNoteListResponse = mutableListOf(
                Note(
                    userId = userId, content = "Hello"
                ),
                Note(
                    userId = userId, content = "Itzik",
                )
            )
            `when`(mockRepository.fetchNotes(userId)).thenReturn(sampleNoteListResponse)
            mockNoteViewModel.fetchNotesForUser(userId)
            advanceUntilIdle()

            val result = mockNoteViewModel.publicNote.value

            assertEquals("10000001",userId)
            assertEquals("How are you",result.content)
        }
    }