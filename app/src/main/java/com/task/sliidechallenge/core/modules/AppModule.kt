package com.task.sliidechallenge.core.modules

import com.task.sliidechallenge.core.datasources.remote.UserApiService
import com.task.sliidechallenge.data.repos.UserRepositoryImpl
import com.task.sliidechallenge.domain.repos.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * @Author Abdullah Abo El~Makarem on 21/07/2024.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideUserApiService(): UserApiService {
        return Retrofit.Builder()
            .baseUrl("https://gorest.co.in")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserRepositoryImpl(apiService: UserApiService): UserRepository {
        return UserRepositoryImpl(apiService)
    }
}