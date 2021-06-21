package com.mongodb.rchatapp.ui.chat


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mongodb.rchatapp.ui.data.ChatMessage
import io.realm.*
import io.realm.kotlin.where
import io.realm.mongodb.App
import io.realm.mongodb.sync.SyncConfiguration
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.AbstractMap

class ChatMessageViewModel(private val realmSync: App, private val conversationId: String) :
    ViewModel() {

    private val _loadingBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val loadingBar: LiveData<Boolean> = _loadingBar


    private val _chatMessages: MutableLiveData<List<ChatMessage>> = MutableLiveData()
    val chatMessage: LiveData<List<ChatMessage>> = _chatMessages

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
                realm.addChangeListener {
                    val messages = realm.where<ChatMessage>().findAll().sort("timestamp").map {
                        it
                    }
                    _chatMessages.value = messages
                }
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
                realm.executeTransactionAsync {
                    it.insert(
                        ChatMessage(
                            text = message,
                            partition = "conversation=${conversationId}",
                            author = "Mohit"
                        )
                    )
                }

                realm.executeTransaction {

                }


            }

            override fun onError(exception: Throwable) {
                super.onError(exception)
                _loadingBar.value = false
                //TODO : need to implement
            }
        })
    }


    open class Player : RealmObject() {
        var name: String? = null
        var email: String? = null
        var playerHandle: String? = null
        var gameplayStats: RealmDictionary<String> = RealmDictionary<String>()
        var competitionStats: RealmDictionary<String> = RealmDictionary<String>()
    }


    fun sampleFunction() {
        val user = realmSync.currentUser() ?: return
        val config = SyncConfiguration.Builder(user, "conversation=${conversationId}").build()

        // Insert
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {




            }

            override fun onError(exception: Throwable) {
                super.onError(exception)
                _loadingBar.value = false
                //TODO : need to implement
            }
        })
    }



}