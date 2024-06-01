package com.itzik.mynotes.project.viewmodels

import androidx.lifecycle.ViewModel
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.repositories.IRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repo: IRepo
) : ViewModel() {


    private suspend fun saveNote(note: Note) = repo.saveNote(note)

    suspend fun updateNote(newChar: String) {
        val note = Note(content = newChar)
        saveNote(note)
    }

    suspend fun fetchNotes(): Flow<MutableList<Note>> {
        val noteList = flow {
            val notes = repo.fetchNotes()
            if (notes.isNotEmpty()) {
                emit(notes)
            } else return@flow
        }
        return noteList
    }

    suspend fun updateIsInTrashBin(note: Note) {
        note.isInTrash = true
        repo.updateIsInTrashBin(note)
        repo.insertSingleNoteIntoRecycleBin(note)
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

    suspend fun emptyTrashBin() = repo.emptyTrashBin()
}

