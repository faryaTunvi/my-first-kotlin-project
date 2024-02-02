package com.fattyleo.mykotlinapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.fattyleo.mykotlinapp.data.room.UserInfoEntity
import com.fattyleo.mykotlinapp.repository.UserInfoRepository
import kotlinx.coroutines.launch

/**
Please Note:
 *Don't keep a reference to a Context that has a shorter lifecycle than your ViewModel! Examples are:
Activity, Fragment,View
 * Keeping a reference can cause a memory leak, e.g. the ViewModel has a reference to a destroyed Activity!
 * All these objects can be destroyed by the operating system and recreated
 * when there's a configuration change, and this can happen many times during the lifecycle of a ViewModel.*/

class UserViewModel(private val repository: UserInfoRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    // Creates a LiveData that has values collected from the origin Flow.
    val allWords: LiveData<List<UserInfoEntity>> = repository.allUsers.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(userInfo: UserInfoEntity) = viewModelScope.launch {
        repository.insert(userInfo)
    }

    fun delete(userid : Int){
        viewModelScope.launch {
            repository.deleteById(userid)
        }
    }
}