package com.mongodb.rchatapp.ui.chatmembers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mongodb.rchatapp.ui.data.CreateNewChatNavigation
import com.mongodb.rchatapp.ui.data.*
import com.mongodb.rchatapp.utils.getSyncConfig
import io.realm.Realm
import io.realm.RealmList
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

    private val _navigation = MutableLiveData<CreateNewChatNavigation>()
    val navigation: LiveData<CreateNewChatNavigation> = _navigation

    init {
        getMemberList()
    }

    private fun getMemberList() {
        _loadingBar.value = true
        val user = realmSync.currentUser() ?: return
        val config = realmSync.getSyncConfig(partition ="all-users=all-the-users")
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

    fun createChatRoom(roomName: String, selectedMembers: List<ChatsterListViewModel>) {
        val members = _members.value ?: return

        val hostUser = selectedMembers.find { it._id == realmSync.currentUser()?.id }?.run {
            Member(userName = this.userName, membershipStatus = "Membership active")
        }

        val selectedIds = selectedMembers
            .filter { it.isSelected || it._id != realmSync.currentUser()?.id }
            .map { it._id }

        val chatMembers = members.filter { selectedIds.contains(it._id) }
        val conversion = Conversation().apply {
            this.displayName = roomName
            this.members = RealmList()
            this.members.addAll(chatMembers.map {
                Member(userName = it.userName)
            })
            this.members.add(hostUser)
        }
        updateConversionToUser(conversion)
    }

    private fun updateConversionToUser(conversation: Conversation) {
        val user = realmSync.currentUser() ?: return
        val config = realmSync.getSyncConfig(partition ="user=${user.id}")

        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val userInfo = realm.where<User>().findFirst()
                realm.beginTransaction()
                userInfo?.apply {
                    conversations.add(conversation)
                }
                realm.commitTransaction()
                _navigation.value = CreateNewChatNavigation.GoToDashboard
            }
        })
    }
}