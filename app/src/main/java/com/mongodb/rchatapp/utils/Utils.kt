package com.mongodb.rchatapp.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import io.realm.mongodb.App
import io.realm.mongodb.User
import io.realm.mongodb.sync.SyncConfiguration

fun EditText.clear() {
    this.text = null
}

fun View.hideKeyboard() {
    val inputMethodManager =
        context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun App.getSyncConfig(partition: String): SyncConfiguration {
    return SyncConfiguration.Builder(currentUser(), partition).waitForInitialRemoteData().build()
}