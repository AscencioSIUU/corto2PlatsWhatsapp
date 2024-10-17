package com.example.privatechatplats.screen

sealed class Screen(val route: String) {
    object LoginScreen : Screen("loginscreen")
    object SignupScreen : Screen("signupscreen")
    object ChatScreen : Screen("chatscreen")
}
