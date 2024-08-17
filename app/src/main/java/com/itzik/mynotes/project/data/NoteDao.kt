package com.itzik.mynotes.project.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.utils.Constants.NOTE_TABLE


@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun saveNote(note: Note)

    @Query("SELECT *FROM $NOTE_TABLE WHERE isInTrash=0")
    suspend fun fetchNotes(): MutableList<Note>

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleNoteIntoRecycleBin(note: Note)

    @Update
    suspend fun setTrash(note: Note)

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNoteListIntoRecycleBin(notes: MutableList<Note>)

    @Query("SELECT *FROM $NOTE_TABLE WHERE isInTrash=1")
    suspend fun fetchTrashedNotes(): MutableList<Note>

    @Query("DELETE FROM $NOTE_TABLE WHERE isInTrash=1")
    suspend fun emptyTrashBin()

    @Update
    suspend fun updateNote(note: Note)

    @Query("SELECT *FROM $NOTE_TABLE WHERE isStarred=1")
    suspend fun fetchStarredNotes(): MutableList<Note>

    @Query("SELECT * FROM $NOTE_TABLE WHERE isInTrash=0 ORDER BY content ASC")
    suspend fun fetchNotesSortedAlphabetically(): MutableList<Note>

    @Query("SELECT * FROM $NOTE_TABLE WHERE isInTrash=0 ORDER BY time DESC")
    suspend fun fetchNotesSortedByDateModified(): MutableList<Note>

    @Delete
    suspend fun deleteNote(note: Note)



}

