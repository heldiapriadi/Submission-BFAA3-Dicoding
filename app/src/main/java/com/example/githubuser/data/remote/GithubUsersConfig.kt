package com.example.githubuser.data.remote

import android.content.Context
import com.example.githubuser.data.UserRepository
import com.example.githubuser.di.DatabaseModule
import com.example.githubuser.di.NetworkModule
import retrofit2.Retrofit

class GithubUsersConfig {
    companion object {
        private fun getInstance(): Retrofit{
            val loggingInterceptor = NetworkModule.provideHTTPLoggingInterceptor()
            val okHttpClient = NetworkModule.provideOkHttpClient(loggingInterceptor)
            return NetworkModule.provideRetrofit(okHttpClient)
        }

        fun provideUserRepository(context: Context): UserRepository{
            val githubRemoteDataSource = GithubRemoteDataSource(getInstance())
            val appDatabase = DatabaseModule.provideAppDatabase(context)
            val userDao = DatabaseModule.provideUserDao(appDatabase)
            return UserRepository(githubRemoteDataSource, userDao)
        }
    }
}