package com.example.privatechatplats.viewmodel

import androidx.compose.runtime.MutableState
import com.example.privatechatplats.data.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.mutableStateOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.privatechatplats.Injection
import com.example.privatechatplats.data.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val userRepository: UserRepository
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // Propiedad para almacenar el usuario autenticado actual
    private val _currentUser = MutableLiveData<FirebaseUser?>()
    val currentUser: LiveData<FirebaseUser?> get() = _currentUser

    init {
        userRepository = UserRepository(auth, Injection.instance())
        loadCurrentUser()  // Cargar el usuario actual al iniciar el ViewModel
    }

    // Estado del resultado de autenticación
    private val _authResult = MutableLiveData<Result<Boolean>>()
    val authResult: LiveData<Result<Boolean>> get() = _authResult

    // Estado para saber si el usuario ya está autenticado
    private val _alreadyLoggedIn = mutableStateOf(false)
    var alreadyLoggedIn: MutableState<Boolean> = _alreadyLoggedIn

    // Función para cargar el usuario actual desde FirebaseAuth
    private fun loadCurrentUser() {
        _currentUser.value = auth.currentUser
    }

    // Función para registrar un nuevo usuario
    fun signUp(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            _authResult.value = userRepository.signUp(email, password, firstName, lastName)
            loadCurrentUser()  // Actualizar el usuario actual después del registro
        }
    }

    // Función para iniciar sesión
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authResult.value = userRepository.login(email, password)
            loadCurrentUser()  // Actualizar el usuario actual después del inicio de sesión
        }
    }

    // Función para verificar si hay un usuario actualmente autenticado
    fun isUserLoggedIn(): Boolean {
        return _currentUser.value != null
    }
}
