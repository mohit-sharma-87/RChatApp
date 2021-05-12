package com.mongodb.rchatapp.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mongodb.rchatapp.ui.data.model.ProfileNavigation
import com.mongodb.rchatapp.ui.data.model.User
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.App
import io.realm.mongodb.sync.SyncConfiguration

class ProfileViewModel(private val realmApp: App) : ViewModel() {

    private val _navigation: MutableLiveData<ProfileNavigation> = MutableLiveData()
    val navigation: LiveData<ProfileNavigation> = _navigation

    private val _loadingBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val loadingBar: LiveData<Boolean> = _loadingBar

    fun onLogout() {
        if (realmApp.currentUser() != null) {
            _loadingBar.postValue(true)
            realmApp.currentUser()?.logOutAsync {
                _loadingBar.postValue(false)
                if (it.isSuccess) {
                    _navigation.postValue(ProfileNavigation.goToHome)
                }
            }
        }
    }

    fun updateDisplayName(name: String) {

        val user = realmApp.currentUser() ?: return
        Log.e("updateDisplayName", "user=${user.id} ")
        val config = SyncConfiguration.Builder(user, "user=${user.id}").build()

        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val userInfo = realm.where<User>().findFirst()
                Log.e("updateDisplayName", "onSuccess: ${realm.where<User>().count()}")
                realm.beginTransaction()
                userInfo?.apply {
                    userPreferences?.apply {
                        displayName = name
                    }
                }
                realm.commitTransaction()
            }
        })
    }

}