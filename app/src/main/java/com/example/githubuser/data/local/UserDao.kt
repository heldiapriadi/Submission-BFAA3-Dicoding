package com.example.githubuser.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubuser.model.User

@Dao
interface UserDao {

    @Query("SELECT * FROM users order by login")
    fun getAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM users WHERE login = :username")
    fun getUser(username: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Delete
     suspend fun deleteUser(user: User)

    @Delete
     fun deleteAllUser(listUser: List<User>)
}