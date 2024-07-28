package com.itzik.mynotes.project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.model.Note.Companion.getCurrentTime
import com.itzik.mynotes.project.repositories.INoteRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repo: INoteRepo,
) : ViewModel() {

    private val privateNote = MutableStateFlow(Note(content = ""))
    val publicNote: StateFlow<Note> get() = privateNote

    private val privateNoteList = MutableStateFlow<MutableList<Note>>(mutableListOf())
    val publicNoteList: StateFlow<MutableList<Note>> get() = privateNoteList

        init {
            viewModelScope.launch {
                fetchNotes()
            }
        }


    fun sayHello(): String {
        return repo.sayHello().uppercase()
    }

    suspend fun updateSelectedNoteContent(newChar: String, noteId: Int? = 0, isPinned: Boolean, isLiked:Boolean) {
        privateNote.value.isPinned = isPinned
        privateNote.value.content = newChar
        if (noteId != null) {
            privateNote.value.id = noteId
        }
        privateNote.value.isLiked=isLiked
        privateNote.value.time = getCurrentTime()
        repo.updateNote(privateNote.value)
    }

    suspend fun saveNote(note: Note) {
        val noteList = repo.fetchNotes()
        val matchingNoteToPreviousVersion = noteList.find {
            it.id == note.id
        }
        if (matchingNoteToPreviousVersion == null) {
            repo.saveNote(note)
        } else {
            updateSelectedNoteContent(note.content, isPinned = note.isPinned, isLiked=note.isLiked)
        }
        fetchNotes()
    }

    private suspend fun fetchNotes() {
        val notes = repo.fetchNotes()
        privateNoteList.value = notes.toMutableList()
    }

    fun setNoteList(notes: MutableList<Note>) {
        privateNoteList.value = notes
    }

    suspend fun setTrash(note: Note) {
        note.isInTrash = true
        note.isLiked=false
        repo.setTrash(note)
        repo.insertSingleNoteIntoRecycleBin(note)
        fetchNotes()
    }

    fun emptyTrashBin() = viewModelScope.launch {
        repo.emptyTrashBin()
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

    suspend fun fetchLikedNotes(): Flow<MutableList<Note>> {
        val noteList = flow {
            val notes = repo.fetchLikedNotes()
            if (notes.isNotEmpty()) {
                emit(notes)
            } else return@flow
        }
        return noteList
    }


    fun toggleLikedButton(note: Note) = viewModelScope.launch {
        val updatedNote = note.copy(isLiked = !note.isLiked)
        repo.updateNote(updatedNote)
        fetchNotes()
    }
}