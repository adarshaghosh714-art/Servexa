package com.example.servexa.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.servexa.model.ChatMessage
import com.example.servexa.viewModel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val ChatPurple = Color(0xFF5B4AEF)
private val ChatPurpleDark = Color(0xFF4835D4)
private val ChatBackground = Color(0xFFF7F7FC)
private val ChatText = Color(0xFF18181B)
private val ChatSecondary = Color(0xFF71717A)
private val IncomingBubble = Color.White

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    conversationId: String,
    otherUserId: String,
    otherUserName: String,
    onBackClick: () -> Unit
) {
    // Observe messages and loading/error states from the ViewModel.
    val messages by viewModel.messages.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Identify the logged-in user to distinguish sent messages.
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    var messageText by remember { mutableStateOf("") }

    val listState = rememberLazyListState()

    // Load messages whenever this conversation changes.
    LaunchedEffect(conversationId) {
        viewModel.loadMessages(conversationId)
    }

    // Keep the newest message visible.
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.lastIndex)
        }
    }

    // Send a message through your existing ViewModel.
    fun sendCurrentMessage() {
        val text = messageText.trim()

        if (text.isNotEmpty() && currentUserId != null) {
            viewModel.sendMessage(
                conversationId = conversationId,
                receiverId = otherUserId,
                text = text
            )

            messageText = ""
        }
    }

    Scaffold(
        containerColor = ChatBackground,
        bottomBar = {
            MessageInputBar(
                messageText = messageText,
                onMessageChange = { messageText = it },
                onSendClick = { sendCurrentMessage() },
                onKeyboardSend = { sendCurrentMessage() }
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF0EEFF),
                            ChatBackground,
                            Color.White
                        )
                    )
                )
        ) {

            // Chat header.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                ChatPurple,
                                ChatPurpleDark
                            )
                        )
                    )
                    .padding(
                        horizontal = 12.dp,
                        vertical = 14.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector =
                            Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Go back",
                        tint = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = otherUserName
                            .trim()
                            .firstOrNull()
                            ?.uppercaseChar()
                            ?.toString() ?: "?",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {
                    Text(
                        text = otherUserName,
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Servexa messages",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }

            // Message history.
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {

                when {
                    isLoading && messages.isEmpty() -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                            color = ChatPurple
                        )
                    }

                    messages.isEmpty() && errorMessage == null -> {
                        EmptyChatState(
                            modifier = Modifier.align(Alignment.Center),
                            userName = otherUserName
                        )
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            state = listState,
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                horizontal = 16.dp,
                                vertical = 20.dp
                            ),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = messages,
                                key = { message ->
                                    message.id.ifBlank {
                                        "${message.senderId}_${message.sentAt}_${message.text}"
                                    }
                                }
                            ) { message ->

                                MessageBubble(
                                    message = message,
                                    isMine = message.senderId == currentUserId
                                )
                            }
                        }
                    }
                }

                // Display errors without hiding the message history.
                if (errorMessage != null) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(12.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFE4E6)
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            modifier = Modifier.padding(12.dp),
                            color = Color(0xFFBE123C),
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(
    message: ChatMessage,
    isMine: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isMine) {
            Arrangement.End
        } else {
            Arrangement.Start
        }
    ) {

        Column(
            modifier = Modifier
                .widthIn(max = 300.dp)
                .background(
                    color = if (isMine) {
                        ChatPurple
                    } else {
                        IncomingBubble
                    },
                    shape = RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp,
                        bottomStart = if (isMine) 18.dp else 4.dp,
                        bottomEnd = if (isMine) 4.dp else 18.dp
                    )
                )
                .padding(
                    horizontal = 15.dp,
                    vertical = 11.dp
                )
        ) {

            Text(
                text = message.text,
                color = if (isMine) Color.White else ChatText,
                fontSize = 15.sp,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = formatMessageTime(message.sentAt),
                modifier = Modifier.align(Alignment.End),
                color = if (isMine) {
                    Color.White.copy(alpha = 0.75f)
                } else {
                    ChatSecondary
                },
                fontSize = 10.sp
            )
        }
    }
}

@Composable
private fun MessageInputBar(
    messageText: String,
    onMessageChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onKeyboardSend: () -> Unit
) {
    Surface(
        color = Color.White,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 12.dp,
                    vertical = 10.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedTextField(
                value = messageText,
                onValueChange = onMessageChange,
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "Write a message...",
                        color = ChatSecondary
                    )
                },
                shape = RoundedCornerShape(26.dp),
                maxLines = 4,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = { onKeyboardSend() }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChatPurple,
                    unfocusedBorderColor = Color(0xFFE4E4E7),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = ChatPurple
                )
            )

            IconButton(
                onClick = onSendClick,
                enabled = messageText.isNotBlank(),
                modifier = Modifier
                    .padding(start = 6.dp)
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (messageText.isNotBlank()) {
                            ChatPurple
                        } else {
                            Color(0xFFE4E4E7)
                        }
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send message",
                    tint = if (messageText.isNotBlank()) {
                        Color.White
                    } else {
                        ChatSecondary
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyChatState(
    modifier: Modifier = Modifier,
    userName: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .size(76.dp)
                .clip(CircleShape)
                .background(Color(0xFFEAE7FF)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ChatBubbleOutline,
                contentDescription = null,
                tint = ChatPurple,
                modifier = Modifier.size(34.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = "Start a conversation",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = ChatText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Send a message to $userName about your service.",
            fontSize = 14.sp,
            color = ChatSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 21.sp
        )
    }
}

private fun formatMessageTime(timestamp: Long): String {
    if (timestamp <= 0L) return ""

    return SimpleDateFormat(
        "h:mm a",
        Locale.getDefault()
    ).format(Date(timestamp))
}



@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ChatScreenPreview() {
    val sampleMessages = listOf(
        ChatMessage(
            id = "1",
            senderId = "customer123",
            receiverId = "provider456",
            text = "Hi, I need help fixing a faulty light bulb.",
            sentAt = System.currentTimeMillis() - 300000
        ),
        ChatMessage(
            id = "2",
            senderId = "provider456",
            receiverId = "customer123",
            text = "Hello! Sure, I can help you with that.",
            sentAt = System.currentTimeMillis() - 240000
        ),
        ChatMessage(
            id = "3",
            senderId = "customer123",
            receiverId = "provider456",
            text = "Can you visit this evening?",
            sentAt = System.currentTimeMillis() - 120000
        ),
        ChatMessage(
            id = "4",
            senderId = "provider456",
            receiverId = "customer123",
            text = "Yes, I should be available around 6 PM.",
            sentAt = System.currentTimeMillis() - 60000
        )
    )

    // Use a fixed sample ID to preview incoming and outgoing bubbles.
    val previewCurrentUserId = "provider456"

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(ChatBackground)
        ) {
            // Sample chat header.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(ChatPurple, ChatPurpleDark)
                        )
                    )
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )

                Box(
                    modifier = Modifier
                        .padding(start = 14.dp)
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "R",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier.padding(start = 12.dp)
                ) {
                    Text(
                        text = "Rahul Sharma",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Servexa messages",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }

            // Sample conversation history.
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 16.dp,
                    vertical = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(sampleMessages) { message ->
                    MessageBubble(
                        message = message,
                        isMine = message.senderId == previewCurrentUserId
                    )
                }
            }

            // Sample input bar. The preview doesn't send messages.
            MessageInputBar(
                messageText = "",
                onMessageChange = {},
                onSendClick = {},
                onKeyboardSend = {}
            )
        }
    }
}
