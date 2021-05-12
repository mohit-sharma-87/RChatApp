package com.mongodb.rchatapp.ui.data.model

sealed class LoginNavigation {
    object goToHome : LoginNavigation()
    object goToProfile : LoginNavigation()
}


sealed class ProfileNavigation {
    object goToHome : ProfileNavigation()
}


