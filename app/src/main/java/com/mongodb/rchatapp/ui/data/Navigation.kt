package com.mongodb.rchatapp.ui.data

sealed class LoginNavigation {
    object goToHome : LoginNavigation()
    object goToProfile : LoginNavigation()
}

sealed class ProfileNavigation {
    object GoToHome : ProfileNavigation()
}

sealed class CreateNewChatNavigation {
    object GoToDashboard : CreateNewChatNavigation()
}

sealed class HomeNavigation {

    data class GoToSelectedRoom(
        val conversationId: String,
        val roomName: String,
        val currentUsername: String
    ) :
        HomeNavigation()

    object GoToProfile : HomeNavigation()
    object GoToLogin : HomeNavigation()
}

