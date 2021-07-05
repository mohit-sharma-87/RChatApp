package com.mongodb.rchatapp.di

import com.mongodb.rchatapp.RChatApplication
import com.mongodb.rchatapp.ui.chat.ChatMessageViewModel
import com.mongodb.rchatapp.ui.chatmembers.ChatMemberViewModel
import com.mongodb.rchatapp.ui.home.HomeViewModel
import com.mongodb.rchatapp.ui.login.LoginViewModel
import com.mongodb.rchatapp.ui.profile.ProfileViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun koinModules() = module {
    viewModel { HomeViewModel((androidApplication() as RChatApplication).realmSync) }
    viewModel { ProfileViewModel((androidApplication() as RChatApplication).realmSync) }
    viewModel { LoginViewModel((androidApplication() as RChatApplication).realmSync) }
    viewModel { ChatMemberViewModel((androidApplication() as RChatApplication).realmSync) }
    viewModel { (conversationId: String, currentUserName: String) ->
        ChatMessageViewModel(
            (androidApplication() as RChatApplication).realmSync,
            conversationId,
            currentUserName
        )
    }
}