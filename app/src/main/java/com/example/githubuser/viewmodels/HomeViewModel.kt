package com.example.githubuser.viewmodels

import androidx.lifecycle.*
import com.example.githubuser.model.GithubSearchResponse
import com.example.githubuser.data.UserRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import com.example.githubuser.utils.Result

class HomeViewModel constructor(private val userRepository: UserRepository): ViewModel() {

    private var _username = MutableLiveData<String>()
    private val _searchResult: LiveData<Result<GithubSearchResponse?>> =_username.distinctUntilChanged().switchMap {
        liveData {
            userRepository.searchUsers(it).onStart {
                emit(Result.loading())
            }.collect {
                emit(it)
            }
        }
    }

    val searchResult = _searchResult

    fun findGithubUser(username: String){
        _username.value = username
    }


}