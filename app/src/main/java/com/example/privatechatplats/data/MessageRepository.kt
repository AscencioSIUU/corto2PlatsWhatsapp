package com.example.privatechatplats.data

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MessageRepository(private val firestore: FirebaseFirestore) {

    suspend fun sendMessage(user1Id: String, user2Id: String, message: Message): Result<Unit> = try {
        val chatId = generateChatId(user1Id, user2Id)
        firestore.collection("chats").document(chatId)
            .collection("messages").add(message).await()
        Result.Success(Unit)
    } catch (e: Exception) {
        Result.Error(e)
    }

    fun getChatMessages(user1Id: String, user2Id: String): Flow<List<Message>> = callbackFlow {
        val chatId = generateChatId(user1Id, user2Id)
        val subscription = firestore.collection("chats").document(chatId)
            .collection("messages")
            .orderBy("timestamp")
            .addSnapshotListener { querySnapshot, _ ->
                querySnapshot?.let {
                    trySend(it.documents.map { doc ->
                        val message = doc.toObject(Message::class.java)!!.copy()
                        val decryptedText = cesarCipher(message.text, shift = -3)  // Desencriptar el mensaje
                        message.copy(text = decryptedText)
                    }).isSuccess
                }
            }
        awaitClose { subscription.remove() }
    }

    private fun generateChatId(user1Id: String, user2Id: String): String {
        return if (user1Id < user2Id) "${user1Id}_${user2Id}" else "${user2Id}_${user1Id}"
    }
}
