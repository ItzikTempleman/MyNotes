package com.itzik.mynotes

import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.repositories.INoteRepo
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock


class NoteViewModelTest {
    private lateinit var mockNoteViewModel: NoteViewModel
    private lateinit var mockNoteRepository: INoteRepo

    @OptIn(ExperimentalCoroutinesApi::class, DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(mainThreadSurrogate)
        mockNoteRepository = mock<INoteRepo>(){
            on {
                sayHello()
            } doReturn ("Hello")
        }
        mockNoteViewModel = NoteViewModel(mockNoteRepository)
    }

    @Test
    fun sayHelloReturnWrongValue(){
        assertNotEquals("Hello", mockNoteViewModel.sayHello())
    }

    @Test
    fun sayHelloReturnUppercase(){
        assertEquals("HELLO", mockNoteViewModel.sayHello())
    }

    @Test
    fun setNoteList(){
        val noteList = mutableListOf<Note>()
        noteList.add(Note(content = "wow"))
        mockNoteViewModel.setNoteList(noteList)
        assertEquals(1, mockNoteViewModel.publicNoteList.value.size)
    }

    @Test
    fun fetchNotes(){
        // TODO: Understand how to mock a suspend function
    }
}