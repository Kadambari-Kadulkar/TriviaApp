package com.example.triviaapp.di

import com.example.triviaapp.network.QusetionApi
import com.example.triviaapp.repository.QuestionRepository
import com.example.triviaapp.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideQuestionApi() : QusetionApi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QusetionApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuestionRepository(api: QusetionApi) = QuestionRepository(api)

}