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

    private val privateNoteList = MutableStateFlow<MutableList<Note>>(mutableListOf())
    val exposedNoteList: StateFlow<MutableList<Note>> get() = privateNoteList

    private val _selectedNote = MutableStateFlow(Note(content = ""))
    val selectedNote: StateFlow<Note> = _selectedNote

    init {
        viewModelScope.launch {
            fetchNotes()
        }
    }

    fun selectNote(note: Note) {
        _selectedNote.value = note
    }

    fun setNoteList(notes: MutableList<Note>) {
        privateNoteList.value = notes
    }

     suspend fun saveNote(note: Note) {
        repo.saveNote(note)
        fetchNotes()
    }


    private suspend fun fetchNotes() {
        privateNoteList.value = repo.fetchNotes()
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

