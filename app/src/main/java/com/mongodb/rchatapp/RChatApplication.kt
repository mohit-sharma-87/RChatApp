package com.mongodb.rchatapp

import android.app.Application
import com.mongodb.rchatapp.di.koinModules
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.mongodb.App
import io.realm.mongodb.AppConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class RChatApplication : Application() {

    val realmSync by lazy {
        App(AppConfiguration.Builder(BuildConfig.RealmAppId).build())
    }


    override fun onCreate() {
        super.onCreate()
        setupRealm()
        setupKoin()
    }

    private fun setupRealm() {
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("rChatDb.db")
            .allowQueriesOnUiThread(false)
            .schemaVersion(1)
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.setDefaultConfiguration(config)
    }

    private fun setupKoin() {
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@RChatApplication)
            modules(modules = koinModules())
        }
    }
}