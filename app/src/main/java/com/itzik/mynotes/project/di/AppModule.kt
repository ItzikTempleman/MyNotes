package com.itzik.mynotes.project.di


import android.content.Context
import androidx.room.Room
import com.itzik.mynotes.project.data.AppDatabase
import com.itzik.mynotes.project.data.NoteDao
import com.itzik.mynotes.project.data.UserDao
import com.itzik.mynotes.project.repositories.AppRepository
import com.itzik.mynotes.project.repositories.AppRepositoryInterface
import com.itzik.mynotes.project.requests.WallpaperService
import com.itzik.mynotes.project.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideAppRepository(
        @Named("user_dao") userDao: UserDao,
        @Named("note_dao") noteDao: NoteDao,
        @Named("wallpaper_service") wallpaperService: WallpaperService,
    ): AppRepositoryInterface {
        return AppRepository(userDao, noteDao,wallpaperService )
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

    @Provides
    @Named("wallpaper_service")
    fun provideRetrofit(): WallpaperService {
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().build()).build()
        return retrofit.create(WallpaperService::class.java)
    }
}

