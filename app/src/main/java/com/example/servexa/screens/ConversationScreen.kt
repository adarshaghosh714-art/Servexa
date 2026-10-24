
package com.example.servexa.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.servexa.model.Conversation
import com.example.servexa.viewModel.ChatViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val ChatPurple = Color(0xFF6255E8)
private val ChatPurpleDark = Color(0xFF5143D6)
private val ChatBackground = Color(0xFFF7F7FC)
private val ChatText = Color(0xFF191927)
private val ChatSecondary = Color(0xFF85859A)
private val ChatBorder = Color(0xFFEEEEF5)
private val ChatLavender = Color(0xFFECE9FF)
private val ChatGreen = Color(0xFF22A06B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversationsScreen(
    viewModel: ChatViewModel,
    onBackClick: () -> Unit,
    onConversationClick: (Conversation, String, String) -> Unit
) {
    val conversations by viewModel.conversations.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(viewModel) {
        viewModel.loadConversations()
    }

    val filteredConversations = remember(conversations, searchQuery, currentUserId) {
        conversations
            .filter { conversation ->
                val otherUserId = conversation.participantIds
                    .firstOrNull { it != currentUserId }
                    .orEmpty()

                val name = conversation.participantNames[otherUserId]
                    ?: "User"

                name.contains(searchQuery, ignoreCase = true) ||
                        conversation.lastMessage.contains(
                            searchQuery,
                            ignoreCase = true
                        ) ||
                        conversation.jobId.contains(
                            searchQuery,
                            ignoreCase = true
                        )
            }
            .sortedByDescending { it.lastMessageAt }
    }

    Scaffold(
        containerColor = ChatBackground
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // Premium purple header
            MessagesHeader(
                conversationCount = conversations.size,
                onBackClick = onBackClick
            )

            // Search bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .padding(top = 20.dp, bottom = 22.dp),
                placeholder = {
                    Text(
                        "Search people or messages",
                        fontSize = 14.sp,
                        color = ChatSecondary
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = ChatPurple
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { searchQuery = "" }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear search",
                                tint = ChatSecondary
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = ChatPurple,
                    unfocusedBorderColor = ChatBorder,
                    cursorColor = ChatPurple
                )
            )

            // Section heading
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Your conversations",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = ChatText
                )

                Spacer(Modifier.weight(1f))

                Text(
                    text = "${filteredConversations.size} chats",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = ChatSecondary
                )
            }

            when {
                isLoading && conversations.isEmpty() -> {
                    LoadingConversations()
                }

                errorMessage != null && conversations.isEmpty() -> {
                    ConversationMessageState(
                        title = "Couldn't load messages",
                        description = errorMessage
                            ?: "Please check your connection and try again.",
                        showRetry = true,
                        onRetry = { viewModel.loadConversations() }
                    )
                }

                filteredConversations.isEmpty() -> {
                    ConversationMessageState(
                        title = if (searchQuery.isNotBlank()) {
                            "No matches found"
                        } else {
                            "No conversations yet"
                        },
                        description = if (searchQuery.isNotBlank()) {
                            "Try searching with another name or keyword."
                        } else {
                            "Your customer and service provider chats will appear here."
                        },
                        showRetry = false,
                        onRetry = {}
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 20.dp,
                            end = 20.dp,
                            bottom = 24.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = filteredConversations,
                            key = { it.id }
                        ) { conversation ->

                            val otherUserId = conversation.participantIds
                                .firstOrNull { it != currentUserId }
                                .orEmpty()

                            val otherUserName =
                                conversation.participantNames[otherUserId]
                                    ?: "User"

                            ConversationCard(
                                conversation = conversation,
                                otherUserName = otherUserName,
                                onClick = {
                                    onConversationClick(
                                        conversation,
                                        otherUserId,
                                        otherUserName
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MessagesHeader(
    conversationCount: Int,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF7568F6),
                        ChatPurpleDark
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .padding(top = 12.dp, bottom = 28.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.16f))
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(Modifier.width(14.dp))

                Column {
                    Text(
                        text = "SERVEXA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                        color = Color.White.copy(alpha = 0.78f)
                    )

                    Text(
                        text = "Messages",
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(Modifier.height(22.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Stay connected.",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )

                    Spacer(Modifier.height(5.dp))

                    Text(
                        text = "Your service conversations, all in one place.",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.82f)
                    )
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White.copy(alpha = 0.17f)
                ) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                            vertical = 12.dp
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = conversationCount.toString(),
                            fontSize = 23.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "CHATS",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationCard(
    conversation: Conversation,
    otherUserName: String,
    onClick: () -> Unit
) {
    val initial = otherUserName
        .trim()
        .firstOrNull()
        ?.uppercaseChar()
        ?.toString() ?: "U"

    val avatarColors = listOf(
        Color(0xFF8A7CF6),
        Color(0xFF5B4AEF)
    )

    val timeText = remember(conversation.lastMessageAt) {
        formatConversationTime(conversation.lastMessageAt)
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 2.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = Color(0x140D0B2A),
                spotColor = Color(0x140D0B2A)
            )
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        color = Color.White,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = ChatBorder
        )
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 15.dp,
                vertical = 17.dp
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Avatar
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .background(
                        brush = Brush.linearGradient(avatarColors),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initial,
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.width(13.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = otherUserName,
                        modifier = Modifier.weight(1f, fill = false),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChatText,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(ChatGreen)
                    )

                    Spacer(Modifier.width(4.dp))

                    Text(
                        text = timeText,
                        fontSize = 10.sp,
                        color = ChatSecondary,
                        maxLines = 1
                    )
                }

                Spacer(Modifier.height(7.dp))

                Text(
                    text = conversation.lastMessage.ifBlank {
                        "Start your conversation"
                    },
                    fontSize = 12.sp,
                    color = ChatSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 17.sp
                )

                Spacer(Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.ChatBubbleOutline,
                        contentDescription = null,
                        tint = ChatPurple,
                        modifier = Modifier.size(12.dp)
                    )

                    Spacer(Modifier.width(5.dp))

                    Surface(
                        color = ChatLavender,
                        shape = RoundedCornerShape(7.dp)
                    ) {
                        Text(
                            text = "Booking ${conversation.jobId}",
                            modifier = Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 4.dp
                            ),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ChatPurpleDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingConversations() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                color = ChatPurple,
                strokeWidth = 3.dp,
                modifier = Modifier.size(34.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Loading your conversations...",
                color = ChatSecondary,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
private fun ConversationMessageState(
    title: String,
    description: String,
    showRetry: Boolean,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(86.dp)
                    .background(
                        color = ChatLavender,
                        shape = RoundedCornerShape(28.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = null,
                    tint = ChatPurple,
                    modifier = Modifier.size(38.dp)
                )
            }

            Spacer(Modifier.height(22.dp))

            Text(
                text = title,
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold,
                color = ChatText
            )

            Spacer(Modifier.height(9.dp))

            Text(
                text = description,
                fontSize = 13.sp,
                color = ChatSecondary,
                lineHeight = 20.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            if (showRetry) {
                Spacer(Modifier.height(20.dp))

                Button(
                    onClick = onRetry,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ChatPurple
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )

                    Spacer(Modifier.width(8.dp))

                    Text("Try again")
                }
            }
        }
    }
}

private fun formatConversationTime(timestamp: Long): String {
    if (timestamp <= 0L) return ""

    val now = System.currentTimeMillis()
    val difference = now - timestamp

    return when {
        difference < 0L -> SimpleDateFormat(
            "dd MMM",
            Locale.getDefault()
        ).format(Date(timestamp))

        difference < 60_000L -> "Just now"

        difference < 24 * 60 * 60 * 1000L -> {
            SimpleDateFormat(
                "hh:mm a",
                Locale.getDefault()
            ).format(Date(timestamp))
        }

        else -> {
            SimpleDateFormat(
                "dd MMM",
                Locale.getDefault()
            ).format(Date(timestamp))
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
fun ConversationsScreenPreview() {
    val sampleConversations = listOf(
        Conversation(
            id = "chat_001",
            participantIds = listOf("customer_1", "provider_1"),
            participantNames = mapOf(
                "customer_1" to "Adarsh Ghosh",
                "provider_1" to "Rahul Sharma"
            ),
            lastMessage = "I'll arrive at 4 PM.",
            lastMessageAt = System.currentTimeMillis(),
            jobId = "JOB-001"
        ),
        Conversation(
            id = "chat_002",
            participantIds = listOf("customer_1", "provider_2"),
            participantNames = mapOf(
                "customer_1" to "Adarsh Ghosh",
                "provider_2" to "Amit Kumar"
            ),
            lastMessage = "Could you share your location?",
            lastMessageAt = System.currentTimeMillis() - 60_000,
            jobId = "JOB-002"
        ),
        Conversation(
            id = "chat_003",
            participantIds = listOf("customer_1", "provider_3"),
            participantNames = mapOf(
                "customer_1" to "Adarsh Ghosh",
                "provider_3" to "Sourav Das"
            ),
            lastMessage = "Thank you for your help!",
            lastMessageAt = System.currentTimeMillis() - 86_400_000,
            jobId = "JOB-003"
        )
    )

    MaterialTheme {
        Scaffold(
            containerColor = ChatBackground
        ) { innerPadding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                MessagesHeader(
                    conversationCount = sampleConversations.size,
                    onBackClick = {}
                )

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .padding(top = 20.dp, bottom = 22.dp),
                    placeholder = {
                        Text(
                            "Search people or messages",
                            fontSize = 14.sp,
                            color = ChatSecondary
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = ChatPurple
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(18.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedBorderColor = ChatPurple,
                        unfocusedBorderColor = ChatBorder,
                        cursorColor = ChatPurple
                    )
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 22.dp)
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Your conversations",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = ChatText
                    )

                    Spacer(Modifier.weight(1f))

                    Text(
                        text = "${sampleConversations.size} chats",
                        fontSize = 12.sp,
                        color = ChatSecondary
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        horizontal = 20.dp,
                        vertical = 4.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = sampleConversations,
                        key = { it.id }
                    ) { conversation ->

                        val otherUserId =
                            conversation.participantIds.first {
                                it != "customer_1"
                            }

                        ConversationCard(
                            conversation = conversation,
                            otherUserName =
                                conversation.participantNames[
                                    otherUserId
                                ] ?: "User",
                            onClick = {}
                        )
                    }
                }
            }
        }
    }
}
