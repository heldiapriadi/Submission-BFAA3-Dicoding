package com.example.githubuser.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.UserRepository
import com.example.githubuser.utils.TypeFollow
import com.example.githubuser.viewmodels.FollowListViewModel

class FollowListViewModelFactory(
    private val userRepository: UserRepository,
    private val username: String,
    private val type: TypeFollow,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(FollowListViewModel::class.java)) {
            FollowListViewModel(userRepository, username, type) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}