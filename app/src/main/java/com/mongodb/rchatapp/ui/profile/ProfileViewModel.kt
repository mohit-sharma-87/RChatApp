package com.mongodb.rchatapp.ui.profile

import androidx.lifecycle.ViewModel
import io.realm.mongodb.App

class ProfileViewModel(private val realmSync: App) : ViewModel() {

        init {
            realmSync.currentUser()

        }



}