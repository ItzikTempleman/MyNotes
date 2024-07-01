package com.itzik.mynotes.project.viewmodels

import android.util.Log
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
    private val repo: InterfaceRepo
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



    suspend fun updateSelectedNoteContent(newChar: String) {
        privateNote.value.content = newChar
        privateNote.value.time=getCurrentTime()
        repo.updateNote(privateNote.value)
    }

    suspend fun saveNote(note: Note) {
        val noteList = repo.fetchNotes()
        val matchingNoteToPreviousVersion = noteList.find {
            Log.d("tag", "viewmodel: it id: ${it.id} and note param id: ${note.id}")
            it.content== note.content
        }
        if (matchingNoteToPreviousVersion == null) {
            repo.saveNote(note)
        } else {
            updateSelectedNoteContent(note.content)
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
}

