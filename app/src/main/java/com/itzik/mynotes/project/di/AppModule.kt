package com.itzik.mynotes.project.di


import android.content.Context
import androidx.room.Room
import com.itzik.mynotes.project.data.AppDatabase
import com.itzik.mynotes.project.data.NoteDao
import com.itzik.mynotes.project.data.UserDao
import com.itzik.mynotes.project.repositories.AppRepository
import com.itzik.mynotes.project.repositories.AppRepositoryInterface
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

