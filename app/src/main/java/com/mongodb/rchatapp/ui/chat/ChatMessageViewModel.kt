package com.mongodb.rchatapp.ui.chat


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mongodb.rchatapp.ui.data.ChatMessage
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults
import io.realm.kotlin.where
import io.realm.mongodb.App
import io.realm.mongodb.sync.SyncConfiguration

class ChatMessageViewModel(private val realmSync: App, private val conversationId: String) :
    ViewModel() {

    private val _loadingBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val loadingBar: LiveData<Boolean> = _loadingBar


    private val _chatMessages: MutableLiveData<List<ChatMessage>> = MutableLiveData()
    val chatMessage: LiveData<List<ChatMessage>> = _chatMessages

    private val _messagePostStatus: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    val messagePostStatus: LiveData<Boolean> = _messagePostStatus


    init {
        getChatList()
    }

    private fun getChatList() {
        _loadingBar.value = true
        val user = realmSync.currentUser() ?: return
        val config = SyncConfiguration.Builder(user, "conversation=${conversationId}").build()

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

        val user = realmSync.currentUser() ?: return
        val config = SyncConfiguration.Builder(user, "conversation=${conversationId}").build()

        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                realm.executeTransactionAsync({
                    it.insert(
                        ChatMessage(
                            text = message,
                            partition = "conversation=${conversationId}",
                            author = "Mohit"
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
}