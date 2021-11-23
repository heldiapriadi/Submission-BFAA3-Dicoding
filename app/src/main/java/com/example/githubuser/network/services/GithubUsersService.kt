package com.example.githubuser.network.services

import com.example.githubuser.model.GithubSearchResponse
import com.example.githubuser.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubUsersService {
    @GET("/search/users")
    suspend fun findUserByUsername(
        @Query("q") username: String
    ): Response<GithubSearchResponse>

    @GET("/users/{username}")
    suspend fun getDetailUserByUsername(
        @Path("username") username: String
    ): Response<User>

    @GET("/users/{username}/followers")
    suspend fun getListFollowers(
        @Path("username") username: String
    ): Response<List<User>>

    @GET("/users/{username}/following")
    suspend fun getListFollowings(
        @Path("username") username: String
    ): Response<List<User>>

}