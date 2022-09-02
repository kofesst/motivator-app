package me.kofesst.android.motivatorapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.kofesst.android.motivatorapp.data.repository.AuthRepositoryImpl
import me.kofesst.android.motivatorapp.data.repository.BaseRepositoryImpl
import me.kofesst.android.motivatorapp.data.repository.PostsRepositoryImpl
import me.kofesst.android.motivatorapp.data.repository.ProfilesRepositoryImpl
import me.kofesst.android.motivatorapp.domain.repository.AuthRepository
import me.kofesst.android.motivatorapp.domain.repository.BaseRepository
import me.kofesst.android.motivatorapp.domain.repository.PostsRepository
import me.kofesst.android.motivatorapp.domain.repository.ProfilesRepository
import me.kofesst.android.motivatorapp.domain.usecases.UseCases
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            context.preferencesDataStoreFile("app_preferences")
        }
    }

    @Provides
    @Singleton
    fun provideBaseRepository(): BaseRepository {
        return BaseRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        baseRepository: BaseRepository,
        dataStore: DataStore<Preferences>,
    ): AuthRepository {
        return AuthRepositoryImpl(baseRepository, dataStore)
    }

    @Provides
    @Singleton
    fun provideProfilesRepository(baseRepository: BaseRepository): ProfilesRepository {
        return ProfilesRepositoryImpl(baseRepository)
    }

    @Provides
    @Singleton
    fun providePostsRepository(baseRepository: BaseRepository): PostsRepository {
        return PostsRepositoryImpl(baseRepository)
    }

    @Provides
    @Singleton
    fun provideUseCases(
        baseRepository: BaseRepository,
        authRepository: AuthRepository,
        profilesRepository: ProfilesRepository,
        postsRepository: PostsRepository,
    ): UseCases {
        return UseCases(
            baseRepository = baseRepository,
            authRepository = authRepository,
            profilesRepository = profilesRepository,
            postsRepository = postsRepository
        )
    }
}