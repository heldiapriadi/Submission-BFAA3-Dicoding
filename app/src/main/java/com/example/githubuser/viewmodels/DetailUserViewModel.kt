package com.example.githubuser.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.UserRepository
import com.example.githubuser.model.User
import com.example.githubuser.utils.Result
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DetailUserViewModel constructor(
    private val userRepository: UserRepository,
    username: String
) : ViewModel() {
    private val _user = MutableLiveData<Result<User?>>()
    val user = _user


    init {
        getDetailUser(username)
    }

    private fun getDetailUser(username: String) {
        viewModelScope.launch {
            userRepository.getDetailUsers(username).collect {
                _user.value = it
            }
        }
    }

    fun addFavorite(user: User) {
        viewModelScope.launch {
            userRepository.insert(user)
        }
    }

    fun removeFavorite(user: User) {
        viewModelScope.launch {
            userRepository.delete(user)
        }
    }

    val isFavorite: LiveData<Boolean> = userRepository.isFavorite

}