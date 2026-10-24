
package com.example.servexa.screens

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.servexa.model.Job
import com.example.servexa.viewModel.JobViewModel

private val ServexaPurple = Color(0xFF6750F5)
private val ServexaLightPurple = Color(0xFFEAE7FF)
private val ServexaBackground = Color(0xFFF8F7FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerRequestsScreen(
    viewModel: JobViewModel,
    onBackClick: () -> Unit = {},
    onInboxClick: () -> Unit = {},
    onChatClick: (Job) -> Unit = {}
) {
    val jobs = viewModel.jobs.collectAsState()
    val context = LocalContext.current
    val errorMessage = viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getCustomerJobs()
    }

    LaunchedEffect(errorMessage.value) {
        errorMessage.value?.let { message ->
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_LONG
            ).show()

            viewModel.clearError()
        }
    }

    Scaffold(
        containerColor = ServexaBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Requests",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your Service Requests",
                    fontSize = 20.sp,
                    lineHeight = 27.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Track the services you have requested",
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            items(
                items = jobs.value,
                key = { it.id }
            ) { job ->
                CustomerRequestCard(
                    job = job,
                    onApprove = {
                        viewModel.approveProvider(job.id)
                    },
                    onChatClick = {
                        onChatClick(job)
                    },
                    onComplete = {
                        viewModel.markJobCompleted(
                            jobId = job.id,
                            isCustomer = true
                        )
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun CustomerRequestCard(
    job: Job,
    onComplete: () -> Unit,
    onApprove: () -> Unit,
    onChatClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            // Job information and status badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(ServexaLightPurple),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = null,
                        tint = ServexaPurple,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = job.title,
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = job.description,
                        fontSize = 13.sp,
                        lineHeight = 18.sp,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = job.providerName.ifBlank {
                            "Waiting for provider"
                        },
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                RequestStatusBadge(
                    status = job.status
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Service provider information
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Service Provider",
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = job.providerName.ifBlank {
                            "Not assigned yet"
                        },
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Budget
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Budget",
                    modifier = Modifier.weight(1f),
                    fontSize = 13.sp,
                    lineHeight = 19.sp,
                    color = Color.Gray
                )

                Text(
                    text = "₹${job.price.toInt()}",
                    fontSize = 17.sp,
                    lineHeight = 23.sp,
                    fontWeight = FontWeight.Bold,
                    color = ServexaPurple
                )
            }

            // Actions available after provider acceptance
            if (job.status == "Accepted") {
                Spacer(modifier = Modifier.height(14.dp))

                if (job.providerId.isNotBlank()) {
                    if (!job.customerApprovedProvider) {
                        Button(
                            onClick = onApprove,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Approve Provider")
                        }
                    } else {
                        Button(
                            onClick = onChatClick,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Chat,
                                contentDescription = null
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text("Chat with Provider")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onComplete,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Mark as Completed")
                }
            }

            if (job.customerApprovedProvider) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Provider approved",
                    color = Color(0xFF238636),
                    fontSize = 12.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun RequestStatusBadge(
    status: String
) {
    val icon: ImageVector
    val backgroundColor: Color
    val textColor: Color

    when (status) {
        "Accepted" -> {
            icon = Icons.Default.CheckCircle
            backgroundColor = Color(0xFFE3F7E8)
            textColor = Color(0xFF238636)
        }

        "Completed" -> {
            icon = Icons.Default.CheckCircle
            backgroundColor = Color(0xFFE3F7E8)
            textColor = Color(0xFF238636)
        }

        else -> {
            icon = Icons.Default.Pending
            backgroundColor = Color(0xFFFFF4D6)
            textColor = Color(0xFF9A6700)
        }
    }

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(backgroundColor)
            .padding(
                horizontal = 9.dp,
                vertical = 6.dp
            ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(13.dp)
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = status,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor,
            maxLines = 1
        )
    }
}
