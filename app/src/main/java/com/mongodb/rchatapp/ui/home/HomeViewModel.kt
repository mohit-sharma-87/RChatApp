package com.mongodb.rchatapp.ui.home

import androidx.lifecycle.*
import com.mongodb.rchatapp.ui.data.model.Conversation
import com.mongodb.rchatapp.ui.data.model.User
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.App
import io.realm.mongodb.sync.SyncConfiguration

class HomeViewModel(private val realmSync: App) : ViewModel(), LifecycleObserver {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _currentUser = MutableLiveData<io.realm.mongodb.User>()

    val isLoggedIn: LiveData<Boolean> = Transformations.map(_currentUser) {
        it != null
    }

    private val _isProfileComplete: MutableLiveData<Boolean> = MutableLiveData()
    val isProfileComplete: LiveData<Boolean> = _isProfileComplete

    private val _loadingBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val loadingBar: LiveData<Boolean> = _loadingBar


    private val _chatList: MutableLiveData<List<Conversation>> = MutableLiveData()
    val chatList: LiveData<List<Conversation>> = _chatList


    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onLoad() {
        _currentUser.value = realmSync.currentUser()
        checkProfileCompletion()
        getChatGroupList()
    }

    private fun checkProfileCompletion() {
        val user = realmSync.currentUser() ?: return
        val config = SyncConfiguration.Builder(user, "user=${user.id}").build()

        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val userInfo = realm.where<User>().findFirst()
                _isProfileComplete.value = userInfo?.userPreferences != null
            }

            override fun onError(exception: Throwable) {
                super.onError(exception)
                //TODO : need to implement
            }
        })

    }

    private fun getChatGroupList() {
        val user = realmSync.currentUser() ?: return
        val config = SyncConfiguration.Builder(user, "user=${user.id}").build()

        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val userInfo = realm.where<User>().findFirst()
                _chatList.value = userInfo?.conversations ?: emptyList()
            }

            override fun onError(exception: Throwable) {
                super.onError(exception)
                _chatList.value = emptyList()
            }
        })
    }

}