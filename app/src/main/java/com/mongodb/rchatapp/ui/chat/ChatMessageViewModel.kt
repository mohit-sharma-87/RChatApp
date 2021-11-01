package com.mongodb.rchatapp.ui.chat


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mongodb.rchatapp.ui.data.ChatMessage
import com.mongodb.rchatapp.ui.data.Conversation
import com.mongodb.rchatapp.ui.data.User
import com.mongodb.rchatapp.utils.getSyncConfig
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import io.realm.kotlin.where
import io.realm.mongodb.App

class ChatMessageViewModel(
    private val realmSync: App,
    private val conversationId: String,
    private val currentUserName: String
) :
    ViewModel() {

    private val _loadingBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val loadingBar: LiveData<Boolean> = _loadingBar

    private val _chatMessages: MutableLiveData<List<ChatMessage>> = MutableLiveData()
    val chatMessage: LiveData<List<ChatMessage>> = _chatMessages

    private val _messagePostStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val messagePostStatus: LiveData<Boolean> = _messagePostStatus

    private val _conversation: MutableLiveData<Conversation> = MutableLiveData()
    val conversation: LiveData<Conversation> = _conversation

    init {
        getChatList()
        getConversionInfo()
    }

    private fun getChatList() {
        _loadingBar.value = true
        val config = realmSync.getSyncConfig(partition = "conversation=${conversationId}")

        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                _loadingBar.value = false

                val realmResults = realm.where<ChatMessage>().findAll().sort("timestamp")
                realmResults.addChangeListener(RealmChangeListener<RealmResults<ChatMessage>> {
                    _chatMessages.value = it.map { it }
                })
                _chatMessages.value = realmResults.map { it }
            }

            override fun onError(exception: Throwable) {
                super.onError(exception)
                _loadingBar.value = false
                //TODO : need to implement
            }
        })
    }

    fun sendChatMessage(message: String) {
        val config = realmSync.getSyncConfig(partition = "conversation=${conversationId}")

        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                realm.executeTransactionAsync({
                    it.insert(
                        ChatMessage(
                            text = message,
                            partition = "conversation=${conversationId}",
                            author = currentUserName
                        )
                    )
                }, {
                    _messagePostStatus.postValue(true)
                }, {
                    _messagePostStatus.postValue(false)
                })
            }

            override fun onError(exception: Throwable) {
                super.onError(exception)
                _loadingBar.value = false
                //TODO : need to implement
            }
        })
    }

    private fun getConversionInfo() {
        val user = realmSync.currentUser() ?: return
        val config = realmSync.getSyncConfig("user=${user.id}")

        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val conversation = realm.where<User>().equalTo("_id", user.id).findFirst()?.let {
                    it.conversations.find { it.id == conversationId }
                }
                _conversation.value = conversation!!
            }

            override fun onError(exception: Throwable) {
                super.onError(exception)
            }
        })


    }
}