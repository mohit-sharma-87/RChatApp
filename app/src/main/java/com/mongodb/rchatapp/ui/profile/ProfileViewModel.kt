package com.mongodb.rchatapp.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.mongodb.rchatapp.ui.data.Photo
import com.mongodb.rchatapp.ui.data.ProfileNavigation
import com.mongodb.rchatapp.ui.data.User
import com.mongodb.rchatapp.utils.SingleLiveEvent
import com.mongodb.rchatapp.utils.getSyncConfig
import io.realm.Realm
import io.realm.kotlin.where
import io.realm.mongodb.App

class ProfileViewModel(private val realmApp: App) : ViewModel() {

    private val _navigation: SingleLiveEvent<ProfileNavigation> = SingleLiveEvent()
    val navigation: SingleLiveEvent<ProfileNavigation> = _navigation

    private val _loadingBar: MutableLiveData<Boolean> = MutableLiveData(false)
    val loadingBar: LiveData<Boolean> = _loadingBar

    private val _user: MutableLiveData<User> = MutableLiveData()
    val userName: LiveData<String> = Transformations.map(_user) {
        it?.userPreferences?.displayName ?: ""
    }

    val userImage: LiveData<ByteArray?> = Transformations.map(_user) {
        it?.userPreferences?.avatarImage?.picture
    }

    init {
        getProfileData()
    }

    fun onLogout() {
        if (realmApp.currentUser() != null) {
            _loadingBar.postValue(true)
            realmApp.currentUser()?.logOutAsync {
                _loadingBar.postValue(false)
                if (it.isSuccess) {
                    _navigation.postValue(ProfileNavigation.GoToHome)
                }
            }
        }
    }

    fun updateDisplayName(name: String) {

        val user = realmApp.currentUser() ?: return
        Log.e("updateDisplayName", "user=${user.id} ")
        val config = realmApp.getSyncConfig(partition = "user=${user.id}")

        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val userInfo = realm.where<User>().findFirst()
                Log.e("updateDisplayName", "onSuccess: ${realm.where<User>().count()}")
                realm.beginTransaction()
                userInfo?.apply {
                    userPreferences?.apply {
                        displayName = name.trim()
                    }
                }
                realm.commitTransaction()
                _navigation.value = ProfileNavigation.GoToHome
            }
        })
    }


    fun updateImage(userImage: ByteArray) {

        val user = realmApp.currentUser() ?: return
        Log.e("updateDisplayName", "user=${user.id} ")
        val config = realmApp.getSyncConfig(partition = "user=${user.id}")

        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val userInfo = realm.where<User>().findFirst()
                Log.e("updateDisplayName", "onSuccess: ${realm.where<User>().count()}")
                realm.beginTransaction()
                userInfo?.apply {
                    userPreferences?.apply {
                        avatarImage = Photo(picture = userImage, thumbNail = userImage)
                    }
                }
                realm.commitTransaction()
                _navigation.value = ProfileNavigation.GoToHome
            }
        })
    }

    private fun getProfileData() {
        val user = realmApp.currentUser() ?: return
        val config = realmApp.getSyncConfig(partition = "user=${user.id}")
        Realm.getInstanceAsync(config, object : Realm.Callback() {
            override fun onSuccess(realm: Realm) {
                val userInfo = realm.where<User>().findFirst()
                _user.value = userInfo
            }
        })
    }

}