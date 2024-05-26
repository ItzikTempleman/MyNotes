package com.itzik.mynotes.project.di



import android.content.Context
import androidx.room.Room
import com.itzik.mynotes.project.data.NoteDao
import com.itzik.mynotes.project.data.NoteDatabase
import com.itzik.mynotes.project.data.UserDao
import com.itzik.mynotes.project.data.UserDatabase
import com.itzik.mynotes.project.utils.Constants.USER_DATABASE
import com.itzik.mynotes.project.repositories.IRepo
import com.itzik.mynotes.project.repositories.RepoImp
import com.itzik.mynotes.project.utils.Constants.NOTE_DATABASE
import com.itzik.mynotes.project.utils.Converters
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideUserConverter(): Converters = Converters()

    @Provides
    @Singleton
    fun provideRepo(repoImpl: RepoImp): IRepo = repoImpl

    @Singleton
    @Provides
    @Named("user_dao")
    fun provideUserDao(userDatabase: UserDatabase): UserDao = userDatabase.getUserDao()

    @Singleton
    @Provides
    fun provideUserDatabase(@ApplicationContext applicationContext: Context) =
        Room.databaseBuilder(applicationContext, UserDatabase::class.java, USER_DATABASE)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .fallbackToDestructiveMigration().build()

    @Singleton
    @Provides
    @Named("note_dao")
    fun provideNoteDao(noteDatabase: NoteDatabase): NoteDao = noteDatabase.getNoteDao()

    @Singleton
    @Provides
    fun provideNoteDatabase(@ApplicationContext applicationContext: Context)=
        Room.databaseBuilder(applicationContext, NoteDatabase::class.java, NOTE_DATABASE)
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .fallbackToDestructiveMigration().build()
}
