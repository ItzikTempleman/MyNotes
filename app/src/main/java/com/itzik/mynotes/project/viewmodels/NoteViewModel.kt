package com.itzik.mynotes.project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.model.Note.Companion.getCurrentTime
import com.itzik.mynotes.project.repositories.InterfaceRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repo: InterfaceRepo,
) : ViewModel() {

    private val privateNoteList = MutableStateFlow<MutableList<Note>>(mutableListOf())
    val publicNoteList: StateFlow<MutableList<Note>> get() = privateNoteList

    private val privateNote = MutableStateFlow(Note(content = ""))
    val publicNote: StateFlow<Note> get() = privateNote


    init {
        viewModelScope.launch {
            fetchNotes()
        }
    }


    suspend fun updateSelectedNoteContent(newChar: String, noteId: Int?=0, isPinned: Boolean) {
        privateNote.value.isPinned=isPinned
        privateNote.value.content = newChar
        if (noteId != null) {
            privateNote.value.id=noteId
        }
        privateNote.value.time=getCurrentTime()
        repo.updateNote(privateNote.value)
    }

    suspend fun saveNote(note: Note) {
        val noteList = repo.fetchNotes()
        val matchingNoteToPreviousVersion = noteList.find {
            it.id== note.id
        }
        if (matchingNoteToPreviousVersion == null) {
            repo.saveNote(note)
        } else {
            updateSelectedNoteContent(note.content, isPinned = note.isPinned)
        }
        fetchNotes()
    }

    private suspend fun fetchNotes() {
        privateNoteList.value = repo.fetchNotes()
    }

    fun setNoteList(notes: MutableList<Note>) {
        privateNoteList.value = notes
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

    fun togglePinned(note: Note) = viewModelScope.launch {
        val updatedNote = note.copy(isPinned = !note.isPinned)
        repo.updateNote(updatedNote)
        fetchNotes()
    }
}

