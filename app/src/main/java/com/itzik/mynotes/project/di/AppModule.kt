package com.itzik.mynotes.project.di


import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.itzik.mynotes.project.data.AppDatabase
import com.itzik.mynotes.project.data.NoteDao
import com.itzik.mynotes.project.data.UserDao
import com.itzik.mynotes.project.repositories.AppRepository
import com.itzik.mynotes.project.repositories.AppRepositoryInterface
import com.itzik.mynotes.project.viewmodels.NoteViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideNoteViewModelFactory(
        factory: NoteViewModelFactory
    ): NoteViewModelFactory {
        return factory
    }

    @Provides
    @Singleton
    fun provideAppRepository(
        @Named("user_dao") userDao: UserDao,
        @Named("note_dao") noteDao: NoteDao
    ): AppRepositoryInterface {
        return AppRepository(userDao, noteDao)
    }

    @Provides
    @Named("user_dao")
    fun provideUserDao(database: AppDatabase): UserDao {
        return database.getUserDao()
    }

    @Provides
    @Named("note_dao")
    fun provideNoteDao(database: AppDatabase): NoteDao {
        return database.getNoteDao()
    }

    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
}


class NoteViewModelFactory @Inject constructor(
    private val userId: String,
    private val appRepository: AppRepositoryInterface
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            return NoteViewModel(appRepository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}