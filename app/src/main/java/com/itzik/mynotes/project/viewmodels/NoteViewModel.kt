package com.itzik.mynotes.project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.repositories.IRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repo: IRepo
) : ViewModel() {

    private val privateNotList = MutableStateFlow<MutableList<Note>>(mutableListOf())
    val exposedNoteList: StateFlow<MutableList<Note>> get() = privateNotList

    init {
        viewModelScope.launch {
            fetchNotes()
        }
    }

    fun setNoteList(notes: MutableList<Note>) {
        privateNotList.value = notes
    }

    private suspend fun saveNote(note: Note) {
        repo.saveNote(note)
        fetchNotes()
    }

    suspend fun updateNoteContent(note: Note, newContent: String) {
        note.content = newContent
        saveNote(note)
    }

    private suspend fun fetchNotes() {
        privateNotList.value = repo.fetchNotes()
    }

    suspend fun updateIsInTrashBin(note: Note) {
        note.isInTrash = true
        repo.updateIsInTrashBin(note)
        repo.insertSingleNoteIntoRecycleBin(note)
        fetchNotes()
    }

    suspend fun fetchTrashedNotes(): Flow<MutableList<Note>> {
        val noteList = flow {
            val notes = repo.fetchTrashedNotes()
            if (notes.isNotEmpty()) {
                emit(notes)
            } else return@flow
        }
        return noteList
    }

    fun emptyTrashBin() = viewModelScope.launch {
        repo.emptyTrashBin()
        fetchNotes()
    }
}

