package com.itzik.mynotes.project.repositories

import com.itzik.mynotes.project.data.NoteDao
import com.itzik.mynotes.project.data.UserDao
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.model.User
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

class NoteRepo @Inject constructor(
    @Named("user_dao")
    @Singleton
    private val userDao: UserDao,
    @Named("note_dao")
    @Singleton
    private val noteDao: NoteDao,
) : INoteRepo {

    override suspend fun insertUser(user: User) = userDao.insertUser(user)
    override suspend fun fetchLoggedInUsers() = userDao.fetchLoggedInUsers()
    override suspend fun getUserFromEmailAndPassword(email: String, password: String) =
        userDao.getUserFromEmailAndPassword(email, password)

    override suspend fun updateIsLoggedIn(user: User) = userDao.updateIsLoggedIn(user)
    override suspend fun updateProfileImage(user: User) = userDao.updateProfileImage(user)

    override suspend fun saveNote(note: Note) = noteDao.saveNote(note)
    override suspend fun fetchNotes() = noteDao.fetchNotes()
    override suspend fun setTrash(note: Note) = noteDao.setTrash(note)
    override suspend fun updateNote(note: Note) = noteDao.updateNote(note)
    override suspend fun insertSingleNoteIntoRecycleBin(note: Note) =
        noteDao.insertSingleNoteIntoRecycleBin(note)

    override suspend fun insertNoteListIntoRecycleBin(notes: MutableList<Note>) =
        noteDao.insertNoteListIntoRecycleBin(notes)

    override suspend fun fetchTrashedNotes() = noteDao.fetchTrashedNotes()
    override suspend fun emptyTrashBin() = noteDao.emptyTrashBin()
    override suspend fun fetchLikedNotes() = noteDao.fetchLikedNotes()
    
    override suspend fun getSortedNotes(sortType: String): MutableList<Note> {
        return when (sortType) {
            "Sort alphabetically" -> noteDao.fetchNotesSortedAlphabetically()
            "Sort by date modified" -> noteDao.fetchNotesSortedByDateModified()
            else -> noteDao.fetchNotes()
        }
    }

    override fun sayHello() = "Hello"

}