package com.example.privatechatplats

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.privatechatplats.screen.ChatScreen
import com.example.privatechatplats.screen.LoginScreen
import com.example.privatechatplats.screen.SignUpScreen
import com.example.privatechatplats.screen.Screen
import com.example.privatechatplats.screen.UserListScreen
import com.example.privatechatplats.ui.theme.PrivateChatPlatsTheme
import com.example.privatechatplats.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val authViewModel: AuthViewModel = viewModel()

            PrivateChatPlatsTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(
                        modifier = Modifier.padding(innerPadding),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        // Llamar al NavigationGraph
                        NavigationGraph(navController = navController, authViewModel = authViewModel)
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Screen.LoginScreen.route
    ) {
        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate(Screen.SignupScreen.route) },
                onSignInSuccess = { navController.navigate(Screen.UserListScreen.route) } // Redirigir a UserListScreen
            )
        }
        composable(Screen.SignupScreen.route) {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.LoginScreen.route) }
            )
        }
        composable(Screen.UserListScreen.route) {
            UserListScreen(onUserClick = { selectedUser ->
                // Al seleccionar un usuario, navegamos a la pantalla de chat
                navController.navigate("${Screen.ChatScreen.route}/${selectedUser.email}")
            })
        }
        composable("${Screen.ChatScreen.route}/{otherUserId}") { backStackEntry ->
            val otherUserId: String = backStackEntry.arguments?.getString("otherUserId") ?: ""
            ChatScreen(
                currentUserId = authViewModel.currentUser.value?.email ?: "",
                otherUserId = otherUserId
            )
        }
    }
}

