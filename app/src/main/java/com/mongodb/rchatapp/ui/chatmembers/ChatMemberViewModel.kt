package com.mongodb.rchatapp.ui.chatmembers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mongodb.rchatapp.ui.data.model.Chatster
import com.mongodb.rchatapp.ui.data.model.ChatsterListViewModel
import com.mongodb.rchatapp.ui.data.model.toListViewModel
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.App
import io.realm.mongodb.sync.SyncConfiguration

class ChatMemberViewModel(private val realmSync: App) : ViewModel() {

    private val _members = MutableLiveData<List<Chatster>>()
    val members: LiveData<List<ChatsterListViewModel>> = Transformations.map(_members) {
        it.map { it.toListViewModel() }
    }

    private val _loadingBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val loadingBar: LiveData<Boolean> = _loadingBar

    init {
        getMemberList()
    }

    private fun getMemberList() {
        _loadingBar.value = true
        val user = realmSync.currentUser() ?: return
        val config = SyncConfiguration.Builder(user, "all-users=all-the-users").build()
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                _loadingBar.value = false
                val users = realm.where<Chatster>().findAll().sort("displayName").map {
                    it
                }
                _members.value = realm.copyFromRealm(users)
            }

            override fun onError(exception: Throwable) {
                super.onError(exception)
                _loadingBar.value = false
                //TODO : need to implement
            }
        })
    }

}