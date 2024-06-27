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
    val publicNoteList: StateFlow<MutableList<Note>> get() = privateNoteList

    private val privateNote = MutableStateFlow(Note(content = ""))
    val publicNote: StateFlow<Note> get() = privateNote

    init {
        viewModelScope.launch {
            fetchNotes()
        }
    }

    fun updateSelectedNote(newChar: String) {
        val note = privateNote.value.copy(content = newChar, time  = Note.getCurrentTime())
        privateNote.value = note
    }

    fun setNoteList(notes: MutableList<Note>) {
        privateNoteList.value = notes
    }

    suspend fun saveNote(note: Note) {
        val existingNotes = repo.fetchNotes()
        val existingNote = existingNotes.find { it.id == note.id }

        if (existingNote == null) {
            repo.saveNote(note)
        } else {
            val updatedNote = note.copy(time = Note.getCurrentTime())
            updateSelectedNote(note.copy(content = "", time = Note.getCurrentTime()))
        }
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

