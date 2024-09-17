package com.itzik.mynotes.project.di


import android.content.Context
import androidx.room.Room
import com.itzik.mynotes.project.data.AppDatabase
import com.itzik.mynotes.project.data.NoteDao
import com.itzik.mynotes.project.data.UserDao
import com.itzik.mynotes.project.repositories.AppRepository
import com.itzik.mynotes.project.repositories.AppRepositoryInterface
import com.itzik.mynotes.project.requests.WeatherApiService
import com.itzik.mynotes.project.utils.API_HOST_NAME
import com.itzik.mynotes.project.utils.API_HOST_VALUE
import com.itzik.mynotes.project.utils.API_KEY_NAME
import com.itzik.mynotes.project.utils.API_KEY_VALUE
import com.itzik.mynotes.project.utils.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
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
        @Named("weather_service") weatherApiService: WeatherApiService
    ): AppRepositoryInterface {
        return AppRepository(userDao, noteDao, weatherApiService)
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
    @Singleton
    @Named("weather_service")
    fun provideRetrofit(): WeatherApiService {
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().addInterceptor(URLInterceptor()).build()).build()
        return retrofit.create(WeatherApiService::class.java)
    }
}

class URLInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request().newBuilder()
            .addHeader(API_KEY_NAME, API_KEY_VALUE)
            .addHeader(API_HOST_NAME, API_HOST_VALUE)
            .build()
        return chain.proceed(request)
    }
}

