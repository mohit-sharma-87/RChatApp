package com.mongodb.rchatapp.ui.data

sealed class LoginNavigation {
    object goToHome : LoginNavigation()
    object goToProfile : LoginNavigation()
}


sealed class ProfileNavigation {
    object GoToHome : ProfileNavigation()
}


