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

    private val privatePinnedNoteList = MutableStateFlow<MutableList<Note>>(mutableListOf())
    val publicLikedNoteList: StateFlow<MutableList<Note>> get() = privatePinnedNoteList


    init {
        viewModelScope.launch {
            fetchNotes()
        }
    }


    fun sayHello(): String {
        return repo.sayHello().uppercase()
    }

    suspend fun updateSelectedNoteContent(newChar: String, noteId: Int? = 0, isPinned: Boolean) {
        privateNote.value.isPinned = isPinned
        privateNote.value.content = newChar
        if (noteId != null) {
            privateNote.value.id = noteId
        }
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

    suspend fun setTrash(note: Note) {
        note.isInTrash = true
        repo.setTrash(note)
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


    fun updatePinnedNoteState(note: Note) {
        if (note.isPinned) {
            privatePinnedNoteList.value.add(note)
            addLikedNote(note)
        } else privatePinnedNoteList.value.remove(note)
    }

    private fun addLikedNote(note: Note) {
        val currentList = privatePinnedNoteList.value
        currentList.remove(note)
        currentList.add(0, note)
        if (currentList.size > 4) {
            currentList.subList(4, currentList.size).clear()
        }
        privateNoteList.value = currentList
    }

    fun fetchPinnedNotesList(notes: MutableList<Note>) {
        privatePinnedNoteList.value = notes
    }


    fun togglePinned(note: Note) = viewModelScope.launch {
        val updatedNote = note.copy(isPinned = !note.isPinned)
        repo.updateNote(updatedNote)
        fetchNotes()
    }
}

