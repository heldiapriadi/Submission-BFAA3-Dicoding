package com.example.githubuser.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubuser.data.UserRepository
import com.example.githubuser.model.User
import com.example.githubuser.utils.Result
import com.example.githubuser.utils.TypeFollow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FollowListViewModel constructor(
    private val userRepository: UserRepository,
    username: String,
    type: TypeFollow
) : ViewModel() {
    private val _listUser = MutableLiveData<Result<List<User?>?>>()
    val listUser = _listUser

    init {
        when (type) {
            TypeFollow.FOLLOWER -> {
                findFollowerUser(username)
            }
            TypeFollow.FOLLOWING -> {
                findFollowingUser(username)
            }
        }

    }

    private fun findFollowerUser(username: String) {
        viewModelScope.launch {
            userRepository.getFollowers(username).collect {
                _listUser.value = it
            }
        }
    }

    private fun findFollowingUser(username: String) {
        viewModelScope.launch {
            userRepository.getFollowings(username).collect {
                _listUser.value = it
            }
        }
    }

}