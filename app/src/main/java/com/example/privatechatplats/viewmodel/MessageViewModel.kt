package com.example.privatechatplats.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.example.privatechatplats.data.Message
import com.example.privatechatplats.data.MessageRepository
import com.example.privatechatplats.data.Result
import com.example.privatechatplats.data.User
import com.example.privatechatplats.data.UserRepository
import com.example.privatechatplats.data.cesarCipher
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MessageViewModel : ViewModel() {
    private val messageRepository: MessageRepository
    private val userRepository: UserRepository

    init {
        messageRepository = MessageRepository(FirebaseFirestore.getInstance())
        userRepository = UserRepository(FirebaseAuth.getInstance(), FirebaseFirestore.getInstance())
        loadCurrentUser()
    }

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    private var currentUserId: String = ""
    private var otherUserId: String = ""

    fun setUserIds(currentUserId: String, otherUserId: String) {
        this.currentUserId = currentUserId
        this.otherUserId = otherUserId
        loadMessages()
    }

    fun sendMessage(text: String) {
        if (_currentUser.value != null) {
            val encryptedText = cesarCipher(text, shift = 3)  // Cifrar el mensaje con el desplazamiento deseado
            val message = Message(
                senderFirstName = _currentUser.value!!.firstName,
                senderId = _currentUser.value!!.email,
                text = encryptedText  // Enviar el mensaje cifrado
            )
            viewModelScope.launch {
                when (messageRepository.sendMessage(currentUserId, otherUserId, message)) {
                    is Result.Success -> Unit
                    is Result.Error -> {
                        // Manejar el error si es necesario
                    }
                }
            }
        }
    }

    fun loadMessages() {
        viewModelScope.launch {
            if (currentUserId.isNotBlank() && otherUserId.isNotBlank()) {
                messageRepository.getChatMessages(currentUserId, otherUserId).collect {
                    _messages.value = it
                }
            }
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser()) {
                is Result.Success -> _currentUser.value = result.data
                is Result.Error -> {
                    // Handle error
                }
            }
        }
    }
}
