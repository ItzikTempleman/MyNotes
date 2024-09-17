package com.itzik.mynotes.project.repositories

import com.itzik.mynotes.project.data.NoteDao
import com.itzik.mynotes.project.data.UserDao
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.model.WeatherResponse
import com.itzik.mynotes.project.requests.WeatherApiService
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

class AppRepository @Inject constructor(
    @Named("user_dao")
    @Singleton
    private val userDao: UserDao,
    @Named("note_dao")
    @Singleton
    private val noteDao: NoteDao,
    @Named("weather_service")
    @Singleton
    private val weatherApiService: WeatherApiService
) : AppRepositoryInterface {

    override suspend fun insertUser(user: User) = userDao.insertUser(user)
    override suspend fun fetchLoggedInUsers() = userDao.fetchLoggedInUsers()
    override suspend fun getUserFromEmailAndPassword(email: String, password: String) =
        userDao.getUserFromEmailAndPassword(email, password)

    override suspend fun updateIsLoggedIn(user: User) = userDao.updateIsLoggedIn(user)
    override suspend fun updateProfileImage(user: User) = userDao.updateProfileImage(user)
    override suspend fun getUserById(userId: String): User = userDao.getUserById(userId)

    override suspend fun saveNote(note: Note) = noteDao.saveNote(note)
    override suspend fun fetchNotes(userId: String) = noteDao.fetchNotes(userId)
    override suspend fun setTrash(note: Note) = noteDao.setTrash(note)
    override suspend fun updateNote(note: Note) = noteDao.updateNote(note)

    override suspend fun insertSingleNoteIntoRecycleBin(note: Note) =
        noteDao.insertSingleNoteIntoRecycleBin(note)

    override suspend fun insertNoteListIntoRecycleBin(notes: MutableList<Note>) =
        noteDao.insertNoteListIntoRecycleBin(notes)

    override suspend fun fetchTrashedNotes(userId: String) = noteDao.fetchTrashedNotes(userId)
    override suspend fun emptyTrashBin() = noteDao.emptyTrashBin()
    override suspend fun fetchStarredNotes(userId: String) = noteDao.fetchStarredNotes(userId)

    override suspend fun getSortedNotes(sortType: String, userId: String): MutableList<Note> {
        return when (sortType) {
            "Sort alphabetically" -> noteDao.fetchNotesSortedAlphabetically(userId)
            "Sort by date modified" -> noteDao.fetchNotesSortedByDateModified(userId)
            else -> noteDao.fetchNotes(userId)
        }
    }

    override suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)

    override suspend fun getWeather(cityName: String): WeatherResponse =weatherApiService.getWeather(cityName)

}