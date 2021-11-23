package com.example.githubuser.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


@Parcelize
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = false)
    @field:SerializedName("id")
    val id: Int,

    @ColumnInfo(name = "repos_url")
    @field:SerializedName("repos_url")
    val reposUrl: String? = null,

    @ColumnInfo(name = "following_url")
    @field:SerializedName("following_url")
    val followingUrl: String? = null,

    @ColumnInfo(name = "login")
    @field:SerializedName("login")
    val login: String? = null,

    @ColumnInfo(name = "public_repos")
    @field:SerializedName("public_repos")
    val publicRepos: Int? = null,

    @ColumnInfo(name = "followers_url")
    @field:SerializedName("followers_url")
    val followersUrl: String? = null,

    @ColumnInfo(name = "url")
    @field:SerializedName("url")
    val url: String? = null,

    @ColumnInfo(name = "followers")
    @field:SerializedName("followers")
    val followers: Int? = null,

    @ColumnInfo(name = "avatar_url")
    @field:SerializedName("avatar_url")
    val avatarUrl: String? = null,

    @ColumnInfo(name = "following")
    @field:SerializedName("following")
    val following: Int? = null,

    @ColumnInfo(name = "name")
    @field:SerializedName("name")
    val name: String? = null,

    @ColumnInfo(name = "location")
    @field:SerializedName("location")
    val location: String? = null,

    @ColumnInfo(name = "html_url")
    @field:SerializedName("html_url")
    val htmlUrl: String? = null,
) : Parcelable
