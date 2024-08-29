package com.itzik.mynotes.project.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.model.Note.Companion.getCurrentTime
import com.itzik.mynotes.project.repositories.AppRepositoryInterface
import com.itzik.mynotes.project.utils.Constants.NAX_PINNED_NOTES
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repo: AppRepositoryInterface,
) : ViewModel() {


    private val privateNote = MutableStateFlow(Note(content = "", userId = "", fontSize = 20))

    val publicNote: StateFlow<Note> get() = privateNote

    private val privateNoteList = MutableStateFlow<MutableList<Note>>(mutableListOf())
    val publicNoteList: StateFlow<MutableList<Note>> get() = privateNoteList

    private val privatePinnedNoteList = MutableStateFlow<MutableList<Note>>(mutableListOf())
    val publicPinnedNoteList: StateFlow<MutableList<Note>> get() = privatePinnedNoteList

    private val privateStarredNoteList = MutableStateFlow<MutableList<Note>>(mutableListOf())
    val publicStarredNoteList: StateFlow<MutableList<Note>> get() = privateStarredNoteList

    private var privatePinStateMap = MutableStateFlow(mapOf<Int, Boolean>())
    val publicPinStateMap: MutableStateFlow<Map<Int, Boolean>> get() = privatePinStateMap

    private var privateStarStateMap = MutableStateFlow(mapOf<Int, Boolean>())
    val publicStarStateMap: MutableStateFlow<Map<Int, Boolean>> get() = privateStarStateMap

    private val privateDeletedNoteList = MutableStateFlow<MutableList<Note>>(mutableListOf())
    val publicDeletedNoteList: StateFlow<MutableList<Note>> get() = privateDeletedNoteList

    var userId=""

        init {
            viewModelScope.launch {
                val users = repo.fetchLoggedInUsers()
                val loggedInUser = users.firstOrNull { it.isLoggedIn }

                if(loggedInUser!=null){
                    userId=loggedInUser.userId
                    privateNote.value = privateNote.value.copy(userId = userId)
                    fetchNotesForUser(userId)
                }else {
                    privateNoteList.value = mutableListOf()
                }

            }
        }

    suspend fun updateSelectedNoteContent(
        newChar: String,
        noteId: Int? = 0,
        isPinned: Boolean,
        isStarred: Boolean,
        fontSize: Int,
        fontColor: Int
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
            privateNote.value.noteId = noteId
        }
        repo.updateNote(privateNote.value)
    }


    suspend fun saveNote(note: Note) {
        val noteList = repo.fetchNotes(userId)
        val matchingNoteToPreviousVersion = noteList.find {
            it.noteId == note.noteId
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
        fetchNotesForUser(userId)
    }

    suspend fun fetchNotesForUser(userId: String) {
        if (userId.isNotEmpty()) {
            val notes = repo.fetchNotes(userId)

            val activeNotes = notes.filter { !it.isInTrash }

            privateNoteList.value = activeNotes.toMutableList()

            val pinMap = activeNotes.associate { it.noteId to it.isPinned }
            val starMap = activeNotes.associate { it.noteId to it.isStarred }
            privatePinStateMap.value = pinMap
            privateStarStateMap.value = starMap
        }else {
            privateNoteList.value = mutableListOf()
        }
    }

    fun setNoteList(notes: MutableList<Note>) {
        privateNoteList.value = notes
    }

    suspend fun setTrash(note: Note) {
        note.isInTrash = true
        note.isStarred = false
        note.isPinned = false
        repo.setTrash(note)
        repo.insertSingleNoteIntoRecycleBin(note)
        fetchNotesForUser(userId)
        fetchDeletedNotes()
    }

    fun emptyTrashBin() = viewModelScope.launch {
        repo.emptyTrashBin()
        fetchDeletedNotes()
    }

    suspend fun getSortedNotes(sortType: String) {
        val sortedNotes = repo.getSortedNotes(sortType, userId)
        privateNoteList.value = sortedNotes.toMutableList()
    }


    fun retrieveNote(note: Note) = viewModelScope.launch {
        note.isInTrash = false
        repo.updateNote(note)
        fetchNotesForUser(userId)
        fetchDeletedNotes()
    }


    suspend fun deleteNotePermanently(note: Note) {
        repo.deleteNote(note)
        fetchDeletedNotes()
    }

    suspend fun fetchDeletedNotes() {
        val notes = repo.fetchTrashedNotes(userId)
        privateDeletedNoteList.value = notes
    }

    suspend fun fetchStarredNotes() = viewModelScope.launch {
        val starredNotes = repo.fetchStarredNotes()
        privateStarredNoteList.value = starredNotes

    }


    suspend fun toggleStarredButton(note: Note) {
        val updatedNote = note.copy(isStarred = !note.isStarred)
        repo.updateNote(updatedNote)
        fetchNotesForUser(userId)
        privateStarStateMap.value = privateStarStateMap.value.toMutableMap().apply {
            put(updatedNote.noteId, updatedNote.isStarred)
        }
        fetchStarredNotes()
    }

    suspend fun togglePinButton(note: Note) {
        if (!note.isPinned && privatePinnedNoteList.value.size >= NAX_PINNED_NOTES) return

        note.isPinned = !note.isPinned
        privatePinStateMap.value = privatePinStateMap.value.toMutableMap().apply {
            put(note.noteId, note.isPinned)
        }
        updatePinnedNotes(note, note.isPinned)
        repo.updateNote(note)
        fetchNotesForUser(userId)
    }

    suspend fun unLikeNote(note: Note) {
        viewModelScope.launch {
            val updatedNote = note.copy(isStarred = false)
            repo.updateNote(updatedNote)
            fetchNotesForUser(userId)
            privateStarStateMap.value = privateStarStateMap.value.toMutableMap().apply {
                put(updatedNote.noteId, updatedNote.isStarred)
            }
            fetchStarredNotes()

        }
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

