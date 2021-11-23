package com.example.githubuser.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.UserRepository
import com.example.githubuser.model.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import com.example.githubuser.utils.Result

class FavoriteViewModel constructor(private val userRepository: UserRepository) : ViewModel() {


    private val _favoriteUsers = MutableLiveData<Result<LiveData<List<User>>>>()
    val favoriteUsers = _favoriteUsers

    init {
        getAllFavoriteUsers()
    }

    private fun getAllFavoriteUsers() {
        viewModelScope.launch {
            userRepository.getAllFavorite().collect {
                _favoriteUsers.value = it
            }
        }
    }
}