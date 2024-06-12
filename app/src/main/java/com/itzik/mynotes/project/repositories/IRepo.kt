package com.itzik.mynotes.project.repositories

import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.model.User


interface IRepo {
    suspend fun insertUser(user: User)
    suspend fun fetchLoggedInUsers(): MutableList<User>
    suspend fun fetchAllUsers(): MutableList<User>
    suspend fun getUserFromEmailAndPassword(email: String, password: String): User
    suspend fun updateIsLoggedIn(user: User)
    suspend fun updateProfileImage(user: User)





    suspend fun saveNote(note: Note)
    suspend fun fetchNotes(): MutableList<Note>
    suspend fun insertSingleNoteIntoRecycleBin(note: Note)
    suspend fun updateIsInTrashBin(note: Note)


    suspend fun insertNoteListIntoRecycleBin(notes: MutableList<Note>)
    suspend fun fetchTrashedNotes():MutableList<Note>
    suspend fun emptyTrashBin()

}
