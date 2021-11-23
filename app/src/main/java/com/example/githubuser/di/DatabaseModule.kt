package com.example.githubuser.di

import android.content.Context
import com.example.githubuser.data.local.AppDatabase
import com.example.githubuser.data.local.UserDao

object DatabaseModule {

    fun provideAppDatabase(appContext: Context): AppDatabase {
        return AppDatabase.getDatabase(appContext)
    }

    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }
}