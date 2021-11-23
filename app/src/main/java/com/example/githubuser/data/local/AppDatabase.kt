package com.example.githubuser.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuser.model.User


@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase{
            val mInstance = INSTANCE
            if (mInstance != null)
                return mInstance

            synchronized(AppDatabase::class){
                val mbuilder = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, "github_user"
                ).build()
                INSTANCE = mbuilder
                return mbuilder
            }
        }
    }
}