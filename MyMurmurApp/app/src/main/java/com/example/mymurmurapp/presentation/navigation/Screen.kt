package com.example.mymurmurapp.presentation.navigation

sealed class Screen(val route: String) {
    object SignIn : Screen("sign_in")
    object SignUp : Screen("sign_up")
    object Timeline : Screen("timeline")
    object MurmurDetail : Screen("murmur_detail/{murmurId}") {
        fun createRoute(murmurId: String) = "murmur_detail/$murmurId"
    }
    object UserProfile : Screen("user_profile/{userId}") {
        fun createRoute(userId: String) = "user_profile/$userId"
    }
    object PostMurmur : Screen("post_murmur")
}

