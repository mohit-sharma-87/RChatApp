package com.mongodb.rchatapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import io.realm.mongodb.App
import io.realm.mongodb.User
import kotlinx.coroutines.flow.DEFAULT_CONCURRENCY

class HomeViewModel(private val realmSync: App) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _currentUser = MutableLiveData<User>()

    val isLoggedIn: LiveData<Boolean> = Transformations.map(_currentUser) {
        it != null
    }

    val isProfileComplete: LiveData<Boolean> = Transformations.map(_currentUser) {
        true
    }

    private val _loadingBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val loadingBar: LiveData<Boolean> = _loadingBar

    init {
        _currentUser.value = realmSync.currentUser()
    }
}