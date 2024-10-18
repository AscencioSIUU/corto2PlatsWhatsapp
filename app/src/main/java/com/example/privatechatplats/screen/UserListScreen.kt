package com.example.privatechatplats.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.privatechatplats.data.User
import com.example.privatechatplats.viewmodel.AuthViewModel

@Composable
fun UserListScreen(
    onUserClick: (User) -> Unit,
    authViewModel: AuthViewModel = viewModel()
) {
    // Cargar la lista de usuarios cuando se inicia la pantalla
    val users = authViewModel.users.observeAsState(emptyList())

    // Cargar todos los usuarios
    authViewModel.loadAllUsers()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn {
            items(users.value) { user ->
                UserItem(user = user, onClick = { onUserClick(user) })
            }
        }
    }
}

@Composable
fun UserItem(user: User, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Text(text = user.email)
    }
}
