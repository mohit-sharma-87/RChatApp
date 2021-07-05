package com.mongodb.rchatapp.ui.home

import android.util.Log
import androidx.lifecycle.*
import com.mongodb.rchatapp.ui.data.HomeNavigation
import com.mongodb.rchatapp.ui.data.Conversation
import com.mongodb.rchatapp.ui.data.User
import com.mongodb.rchatapp.utils.SingleLiveEvent
import com.mongodb.rchatapp.utils.getSyncConfig
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.App

class HomeViewModel(private val realmSync: App) : ViewModel(), LifecycleObserver {

    private val TAG = "HomeViewModel"

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text

    private val _loadingBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val loadingBar: LiveData<Boolean> = _loadingBar


    private val _chatList: MutableLiveData<List<Conversation>> = MutableLiveData()
    val chatList: LiveData<List<Conversation>> = _chatList

    private val _navigation: SingleLiveEvent<HomeNavigation> = SingleLiveEvent()
    val navigation: SingleLiveEvent<HomeNavigation> = _navigation

    private val _currentUser: MutableLiveData<User> = MutableLiveData()
    val userName: MutableLiveData<String> = MutableLiveData()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private fun onLoad() {
        if (realmSync.currentUser() == null) {
            _navigation.value = HomeNavigation.GoToLogin
            return
        }
        getChatGroupList()
    }

    private fun checkProfileCompletion() {
        val user = realmSync.currentUser() ?: return
        val config = realmSync.getSyncConfig(partition = "user=${user.id}")

        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val userInfo = realm.where<User>().findFirst()
                if (userInfo?.userPreferences?.displayName.isNullOrBlank()) {
                    _navigation.value = HomeNavigation.GoToProfile
                }

            }

            override fun onError(exception: Throwable) {
                super.onError(exception)
                //TODO : need to implement
                Log.e(TAG, "onError: ${exception.printStackTrace()}")
            }
        })

    }

    private fun getChatGroupList() {
        val user = realmSync.currentUser() ?: return
        val config = realmSync.getSyncConfig("user=${user.id}")
        _loadingBar.value = true

        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val userInfo = realm.where<User>().findFirst()?.let {
                    _currentUser.value = it
                    checkProfileCompleteStatus(it)
                    updateCurrentUserName(it)
                    realm.copyFromRealm(it)
                }

                Log.e(TAG, "getChatGroupList - onSuccess: ${userInfo?.conversations?.size}")
                _chatList.value = userInfo?.conversations ?: emptyList()
                _loadingBar.value = false
            }

            override fun onError(exception: Throwable) {
                super.onError(exception)
                _chatList.value = emptyList()
                _loadingBar.value = false
                //TODO : need to implement
            }
        })
    }

    fun onRoomClick(it: Conversation) {
        val currentUsername = userName.value ?: return
        _navigation.value =
            HomeNavigation.GoToSelectedRoom(
                conversationId = it.id,
                roomName = it.displayName,
                currentUsername = currentUsername
            )
    }

    private fun updateCurrentUserName(user: User) {
        userName.value = user.userPreferences?.displayName ?: user.userName
    }

    private fun checkProfileCompleteStatus(userInfo: User) {
        if (userInfo.userPreferences?.displayName.isNullOrBlank()) {
            _navigation.value = HomeNavigation.GoToProfile
        }
    }


}