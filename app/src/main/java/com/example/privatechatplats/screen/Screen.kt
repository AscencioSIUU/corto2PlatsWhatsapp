package com.example.privatechatplats.screen

sealed class Screen(val route: String) {
    object LoginScreen : Screen("LoginScreen")
    object SignupScreen : Screen("SignUpScreen")
    object ChatScreen : Screen("ChatScreen")
    object UserListScreen : Screen("UserListScreen")
}
