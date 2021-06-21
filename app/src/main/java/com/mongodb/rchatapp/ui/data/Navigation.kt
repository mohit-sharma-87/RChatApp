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

    object goToLogin : HomeNavigation()
    object goToCreateNewRoom : HomeNavigation()
    data class goToSelectedChatRoom(val conversationId: String, val roomName: String) : HomeNavigation()
}

