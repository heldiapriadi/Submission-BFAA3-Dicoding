package com.example.githubuser.helper

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.githubuser.data.UserRepository
import com.example.githubuser.viewmodels.DetailUserViewModel

class DetailUserViewModelFactory(
    private val userRepository: UserRepository,
    private val username: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DetailUserViewModel::class.java)) {
            DetailUserViewModel(userRepository, username) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}