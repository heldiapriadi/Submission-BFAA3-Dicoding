package com.example.githubuser.data.remote

import com.example.githubuser.model.GithubSearchResponse
import com.example.githubuser.model.User
import com.example.githubuser.network.services.GithubUsersService
import com.example.githubuser.utils.ErrorUtils
import retrofit2.Response
import retrofit2.Retrofit
import com.example.githubuser.utils.Result


class GithubRemoteDataSource constructor(private val retrofit: Retrofit) {
    suspend fun searchUsers(query: String): Result<GithubSearchResponse?> {
        val githubUserService = retrofit.create(GithubUsersService::class.java)
        return getResponse(
            request = { githubUserService.findUserByUsername(query)},
            defaultErrorMessage = "Error fetching Github User List")
    }

    suspend fun getDetailUsers(username: String): Result<User?>{
        val githubUserService = retrofit.create(GithubUsersService::class.java)
        return getResponse(
            request = { githubUserService.getDetailUserByUsername(username)},
            defaultErrorMessage = "Error fetching detail github user")
    }

    suspend fun getFollowers(username: String): Result<List<User>?> {
        val githubUserService = retrofit.create(GithubUsersService::class.java)
        return getResponse(
            request = { githubUserService.getListFollowers(username)},
            defaultErrorMessage = "Error fetching follower user")
    }

    suspend fun getFollowings(username: String): Result<List<User>?> {
        val githubUserService = retrofit.create(GithubUsersService::class.java)
        return getResponse(
            request = { githubUserService.getListFollowings(username)},
            defaultErrorMessage = "Error fetching follower user")
    }

    private suspend fun <T> getResponse(request: suspend () -> Response<T>, defaultErrorMessage: String): Result<T?> {
        return try {
            println("I'm working in thread ${Thread.currentThread().name}")
            val result = request.invoke()
            if (result.isSuccessful) {
                return Result.success(result.body())
            } else {
                val errorResponse = ErrorUtils.parseError(result, retrofit)
                Result.error(errorResponse.status_message ?: defaultErrorMessage, errorResponse)
            }
        } catch (e: Throwable) {
            println(e.message)
            Result.error("Unknown Error", null)
        }
    }
}