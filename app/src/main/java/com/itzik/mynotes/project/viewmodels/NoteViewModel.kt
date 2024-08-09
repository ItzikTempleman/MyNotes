package com.itzik.mynotes.project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.model.Note.Companion.getCurrentTime
import com.itzik.mynotes.project.repositories.INoteRepo
import com.itzik.mynotes.project.utils.Constants.NAX_PINNED_NOTES
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


    private val privateNote = MutableStateFlow(Note(content = "", fontSize = 20))
    val publicNote: StateFlow<Note> get() = privateNote

    private val privateNoteList = MutableStateFlow<MutableList<Note>>(mutableListOf())
    val publicNoteList: StateFlow<MutableList<Note>> get() = privateNoteList

    private val privatePinnedNoteList = MutableStateFlow<MutableList<Note>>(mutableListOf())
    val publicPinnedNoteList: StateFlow<MutableList<Note>> get() = privatePinnedNoteList

    private var privatePinStateMap = MutableStateFlow(mapOf<Int, Boolean>())
    val publicPinStateMap: MutableStateFlow<Map<Int, Boolean>> get() = privatePinStateMap

    private var privateStarStateMap = MutableStateFlow(mapOf<Int, Boolean>())
    val publicStarStateMap: MutableStateFlow<Map<Int, Boolean>> get() = privateStarStateMap


    init {
        viewModelScope.launch {
            fetchNotes()
        }
    }

    fun sayHello(): String {
        return repo.sayHello().uppercase()
    }

    suspend fun updateSelectedNoteContent(
        newChar: String,
        noteId: Int? = 0,
        isPinned: Boolean,
        isStarred: Boolean,
        fontSize: Int,
        fontColor:Int
    ) {

        privateNote.value = privateNote.value.copy(
             fontSize = fontSize,
            fontColor = fontColor,
            isPinned = isPinned,
            isStarred = isStarred,
            content = newChar,
            time = getCurrentTime(),
        )

        if (noteId != null) {
            privateNote.value.id = noteId
        }
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
            updateSelectedNoteContent(
                newChar = note.content,
                isPinned = note.isPinned,
                isStarred = note.isStarred,
                fontSize = note.fontSize,
                fontColor = note.fontColor
            )
        }
        fetchNotes()
    }

    private suspend fun fetchNotes() {
        val notes = repo.fetchNotes()

        val activeNotes = notes.filter { !it.isInTrash }

        privateNoteList.value = activeNotes.toMutableList()

        val pinMap = activeNotes.associate { it.id to it.isPinned }
        val starMap = activeNotes.associate { it.id to it.isStarred }
        privatePinStateMap.value = pinMap
        privateStarStateMap.value = starMap
    }

    fun setNoteList(notes: MutableList<Note>) {
        privateNoteList.value = notes
    }

    suspend fun setTrash(note: Note) {
        note.isInTrash = true
        note.isStarred = false
        note.isPinned=false
        repo.setTrash(note)
        repo.insertSingleNoteIntoRecycleBin(note)
        fetchNotes()
    }

    fun emptyTrashBin() = viewModelScope.launch {
        repo.emptyTrashBin()
        fetchNotes()
    }

    suspend fun getSortedNotes(sortType: String) {
        val sortedNotes = repo.getSortedNotes(sortType)
        privateNoteList.value = sortedNotes.toMutableList()
    }


    fun fetchTrashedNotes(): Flow<MutableList<Note>> {
        val noteList = flow {
            val notes = repo.fetchTrashedNotes()
            if (notes.isNotEmpty()) {
                emit(notes)
            } else return@flow
        }
        return noteList
    }

    suspend fun fetchStarredNotes(): Flow<MutableList<Note>> {
        val noteList = flow {
            val notes = repo.fetchStarredNotes()
            if (notes.isNotEmpty()) {
                emit(notes)
            } else return@flow
        }
        return noteList
    }

    suspend fun toggleStarredButton(note: Note) {
        note.isStarred = !note.isStarred
        privateStarStateMap.value = privateStarStateMap.value.toMutableMap().apply {
            put(note.id, note.isStarred)
        }
        repo.updateNote(note)
        fetchNotes()
    }

    suspend fun togglePinButton(note: Note) {
        if (!note.isPinned && privatePinnedNoteList.value.size >= NAX_PINNED_NOTES) return

        note.isPinned = !note.isPinned
        privatePinStateMap.value = privatePinStateMap.value.toMutableMap().apply {
            put(note.id, note.isPinned)
        }
        updatePinnedNotes(note, note.isPinned)
        repo.updateNote(note)
        fetchNotes()
    }

    private fun updatePinnedNotes(note: Note, isPinned: Boolean) {
        if (isPinned) {
            privatePinnedNoteList.value.add(note)
            privateNoteList.value.remove(note)
        } else {
            privatePinnedNoteList.value.remove(note)
            privateNoteList.value.add(note)
        }
    }
}