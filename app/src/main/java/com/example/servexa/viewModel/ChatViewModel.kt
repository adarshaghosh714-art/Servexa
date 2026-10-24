package com.example.servexa.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servexa.model.ChatMessage
import com.example.servexa.model.Conversation
import com.example.servexa.repository.ChatRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    private val _conversations =
        MutableStateFlow<List<Conversation>>(emptyList())
    val conversations = _conversations.asStateFlow()

    private val _messages =
        MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages = _messages.asStateFlow()

    private val _isLoading =
        MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage =
        MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private var messagesJob: Job? = null

    // Observe the logged-in user's conversations.
    fun loadConversations() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            repository.observeConversations()
                .catch { exception ->
                    _errorMessage.value =
                        exception.message ?: "Failed to load conversations"
                    _isLoading.value = false
                }
                .collect { conversationList ->
                    _conversations.value = conversationList
                    _isLoading.value = false
                }
        }
    }

    // Observe messages in one conversation.
    fun loadMessages(conversationId: String) {
        messagesJob?.cancel()

        messagesJob = viewModelScope.launch {
            _messages.value = emptyList()
            _errorMessage.value = null
            _isLoading.value = true

            repository.observeMessages(conversationId)
                .catch { exception ->
                    _errorMessage.value =
                        exception.message ?: "Failed to load messages"
                    _isLoading.value = false
                }
                .collect { messageList ->
                    _messages.value = messageList
                    _isLoading.value = false
                }
        }
    }

    // Start or reopen a chat.
//    fun getOrCreateConversation(
//        otherUserId: String,
//        otherUserName: String,
//        jobId: String,
//        onSuccess: (String) -> Unit
//    ) {
//        viewModelScope.launch {
//            _errorMessage.value = null
//
//            repository.getOrCreateConversation(
//                otherUserId = otherUserId,
//                otherUserName = otherUserName,
//                jobId = jobId
//            ).onSuccess { conversationId ->
//                onSuccess(conversationId)
//            }.onFailure { exception ->
//                _errorMessage.value =
//                    exception.message ?: "Failed to open conversation"
//            }
//        }
//    }


    fun getOrCreateConversation(
        otherUserId: String,
        otherUserName: String,
        jobId: String,
        onSuccess: (String) -> Unit
    ) {
        viewModelScope.launch {
            _errorMessage.value = null

            repository.getOrCreateConversation(
                otherUserId = otherUserId,
                otherUserName = otherUserName,
                jobId = jobId
            ).onSuccess { conversationId ->
                android.util.Log.d(
                    "ServexaChat",
                    "Conversation opened: $conversationId"
                )
                onSuccess(conversationId)
            }.onFailure { exception ->
                android.util.Log.e(
                    "ServexaChat",
                    "Failed to open conversation. jobId=$jobId, otherUserId=$otherUserId",
                    exception
                )

                _errorMessage.value =
                    exception.message ?: "Failed to open conversation"
            }
        }
    }


    // Send one message.
    fun sendMessage(
        conversationId: String,
        receiverId: String,
        text: String
    ) {
        viewModelScope.launch {
            _errorMessage.value = null

            repository.sendMessage(
                conversationId = conversationId,
                receiverId = receiverId,
                text = text
            ).onFailure { exception ->
                _errorMessage.value =
                    exception.message ?: "Failed to send message"
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    override fun onCleared() {
        messagesJob?.cancel()
        super.onCleared()
    }
}
