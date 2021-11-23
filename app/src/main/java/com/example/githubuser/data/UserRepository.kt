package com.example.githubuser.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuser.data.local.UserDao
import com.example.githubuser.data.remote.GithubRemoteDataSource
import com.example.githubuser.model.GithubSearchResponse
import com.example.githubuser.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.githubuser.utils.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn

class UserRepository(
    private val githubRemoteDataSource: GithubRemoteDataSource,
    private val userDao: UserDao
) {
    private val _favorite: MutableLiveData<Boolean> = MutableLiveData()
    val isFavorite: LiveData<Boolean> = _favorite

    suspend fun searchUsers(query: String): Flow<Result<GithubSearchResponse?>> {
        return flow {
            emit(Result.loading())
            emit(githubRemoteDataSource.searchUsers(query))
        }
    }

    suspend fun getDetailUsers(username: String): Flow<Result<User?>>{
        return flow {
            emit(Result.loading())
            val user = userDao.getUser(username)
            if(user != null){
                emit(Result.success(user))
                _favorite.postValue(true)
            } else{
                emit(githubRemoteDataSource.getDetailUsers(username))
                _favorite.postValue(false)
            }
        }.flowOn(Dispatchers.IO)
    }

    suspend fun getFollowers(username: String): Flow<Result<List<User>?>>{
        return flow {
            emit(Result.loading())
            emit(githubRemoteDataSource.getFollowers(username))
        }
    }

    suspend fun getFollowings(username: String): Flow<Result<List<User>?>>{
        return flow {
            emit(Result.loading())
            emit(githubRemoteDataSource.getFollowings(username))
        }
    }

    suspend fun getAllFavorite(): Flow<Result<LiveData<List<User>>>> {
        return flow{
            emit(Result.loading())
            emit(Result.success(userDao.getAllUsers()))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun insert(user: User){
        userDao.insertUser(user)
        _favorite.value = true
    }

    suspend fun delete(user: User){
        userDao.deleteUser(user)
        _favorite.value = false
    }

}