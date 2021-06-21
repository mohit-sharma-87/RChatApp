package com.mongodb.rchatapp.ui.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*
import java.util.Date;
import io.realm.RealmList;
import io.realm.annotations.RealmClass
import io.realm.annotations.Required

open class Chatster(
    @PrimaryKey var _id: String = "",
    var avatarImage: Photo? = null,
    var displayName: String? = null,
    var lastSeenAt: Date? = null,
    var partition: String = "",
    var presence: String = "",
    var userName: String = ""
) : RealmObject() {}

fun Chatster.toListViewModel(): ChatsterListViewModel {
    return ChatsterListViewModel(
        this._id,
        this.avatarImage,
        this.displayName,
        this.lastSeenAt,
        this.presence,
        this.userName,
        false
    )
}

data class ChatsterListViewModel(
    var _id: String = "",
    var avatarImage: Photo? = null,
    var displayName: String? = null,
    var lastSeenAt: Date? = null,
    var presence: String = "",
    var userName: String = "",
    var isSelected: Boolean = false
)

open class User(
    @PrimaryKey var _id: String = "",
    var conversations: RealmList<Conversation> = RealmList(),
    var lastSeenAt: Date? = null,
    var partition: String = "",
    var presence: String = "",
    var userName: String = "",
    var userPreferences: UserPreferences? = null
) : RealmObject() {}

@RealmClass(embedded = true)
open class Conversation(
    var id: String = UUID.randomUUID().toString(),
    var displayName: String = "",
    var members: RealmList<Member> = RealmList(),
    var unreadCount: Long = 0
) : RealmObject() {}

@RealmClass(embedded = true)
open class Member(
    var membershipStatus: String = "User added, but invite pending",
    var userName: String = ""
) : RealmObject() {}

@RealmClass(embedded = true)
open class UserPreferences(
    var avatarImage: Photo? = null,
    var displayName: String? = null
) : RealmObject() {}

@RealmClass(embedded = true)
open class Photo(
    var date: Date = Date(),
    var picture: ByteArray? = null,
    var thumbNail: ByteArray? = null
) : RealmObject() {}

open class ChatMessage(
    @PrimaryKey var _id: String = UUID.randomUUID().toString(),
    var author: String? = null,
    var image: Photo? = null,
    @Required
    var location: RealmList<Double> = RealmList(),
    var partition: String = "",
    var text: String = "",
    var timestamp: Date = Date()
) : RealmObject() {}