package com.itzik.mynotes.project.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.itzik.mynotes.project.model.Note
import com.itzik.mynotes.project.model.User
import com.itzik.mynotes.project.utils.Converters


@Database(entities = [User::class], version = 3, exportSchema = false )
@TypeConverters(Converters::class)
abstract class UserDatabase:RoomDatabase() {
    abstract fun getUserDao(): UserDao
}

@Database(entities = [Note::class], version = 3, exportSchema = false )
abstract class NoteDatabase:RoomDatabase(){
    abstract fun getNoteDao(): NoteDao
}
