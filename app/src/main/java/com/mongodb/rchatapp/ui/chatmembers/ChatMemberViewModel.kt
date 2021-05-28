package com.mongodb.rchatapp.ui.chatmembers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mongodb.rchatapp.ui.data.model.Chatster
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.App
import io.realm.mongodb.sync.SyncConfiguration

class ChatMemberViewModel(private val realmSync: App) : ViewModel() {

    private val _members = MutableLiveData<List<Chatster>>()
    val members: LiveData<List<Chatster>> = _members

    init {
        getMemberList()
    }

    private fun getMemberList() {
        val user = realmSync.currentUser() ?: return
        val config = SyncConfiguration.Builder(user, "all-users=all-the-users").build()

        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val users = realm.where<Chatster>().findAll().sort("displayName").map {
                    it
                }
                _members.value = users
            }

            override fun onError(exception: Throwable) {
                super.onError(exception)
                //TODO : need to implement
            }
        })
    }

}