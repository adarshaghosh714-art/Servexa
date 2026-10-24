
package com.example.servexa.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.WorkOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.servexa.model.Job
import com.example.servexa.viewModel.JobViewModel

private val ServexaPrimary = Color(0xFF5B5FEF)
private val ServexaInk = Color(0xFF202744)
private val ServexaMuted = Color(0xFF858BA0)
private val ServexaBackground = Color(0xFFF7F8FC)
private val ServexaBorder = Color(0xFFE9EBF3)
private val ServexaPurpleLight = Color(0xFFEEEEFF)
private val ServexaGreen = Color(0xFF168653)
private val ServexaGreenLight = Color(0xFFE7F7EF)
private val ServexaOrange = Color(0xFFB86B0B)
private val ServexaOrangeLight = Color(0xFFFFF2DE)
private val ServexaRed = Color(0xFFBD4141)
private val ServexaRedLight = Color(0xFFFFEEEE)
private val ServexaPurple = Color(0xFF7657F5)
private val ServexaSecondaryText = Color(0xFF71717A)

@Composable
fun JobsScreen(
    viewModel: JobViewModel,
    onJobClick: (Job) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onJobsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onChatClick: (Job) -> Unit = {}
) {
    var searchText by remember { mutableStateOf("") }

    val jobs by viewModel.jobs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.getJobs()
    }

    val filteredJobs = jobs.filter { job ->
        job.title.contains(searchText, ignoreCase = true) ||
                job.customerName.contains(searchText, ignoreCase = true) ||
                job.description.contains(searchText, ignoreCase = true) ||
                job.status.contains(searchText, ignoreCase = true)
    }

    val pendingCount = jobs.count {
        it.status.equals("Pending", ignoreCase = true)
    }

    val acceptedCount = jobs.count {
        it.status.equals("Accepted", ignoreCase = true)
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            JobsBottomBar(
                onHomeClick = onHomeClick,
                onJobsClick = onJobsClick,
                onProfileClick = onProfileClick
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            JobsTopHeader(
                jobCount = jobs.size,
                pendingCount = pendingCount,
                acceptedCount = acceptedCount
            )

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                placeholder = {
                    Text(
                        text = "Search jobs, customers...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 14.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search jobs",
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                    cursorColor = MaterialTheme.colorScheme.primary
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp)
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = if (searchText.isBlank()) "Your jobs" else "Search results",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = "${filteredJobs.size} found",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = ServexaPrimary
                        )
                    }
                }

                errorMessage != null -> {
                    MessageState(
                        title = "Couldn't load your jobs",
                        message = errorMessage ?: "Please try again.",
                        actionLabel = "Try again",
                        onAction = {
                            viewModel.clearError()
                            viewModel.getJobs()
                        }
                    )
                }

                filteredJobs.isEmpty() -> {
                    MessageState(
                        title = if (searchText.isBlank()) {
                            "No jobs to show"
                        } else {
                            "No matching jobs"
                        },
                        message = if (searchText.isBlank()) {
                            "Jobs assigned to your account will appear here."
                        } else {
                            "Try another job title, customer name, or status."
                        },
                        actionLabel = if (searchText.isNotBlank()) {
                            "Clear search"
                        } else {
                            null
                        },
                        onAction = {
                            searchText = ""
                        }
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(
                            start = 20.dp,
                            end = 20.dp,
                            top = 2.dp,
                            bottom = 24.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(
                            items = filteredJobs,
                            key = { job -> job.id }
                        ) { job ->
                            JobCard(
                                job = job,
                                onClick = { onJobClick(job) },
                                onAccept = {
                                    viewModel.updateJobStatus(
                                        job.id,
                                        "Accepted"
                                    )
                                },
                                onReject = {
                                    viewModel.updateJobStatus(
                                        job.id,
                                        "Rejected"
                                    )
                                },
                                onComplete = {
                                    viewModel.markJobCompleted(
                                        jobId = job.id,
                                        isCustomer = false
                                    )
                                },
                                onChatClick = {
                                    onChatClick(job)
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
private fun JobsTopHeader(
    jobCount: Int,
    pendingCount: Int,
    acceptedCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(
                start = 22.dp,
                end = 22.dp,
                top = 24.dp,
                bottom = 20.dp
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "Work overview",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(5.dp))

                Text(
                    text = "My Jobs",
                    fontSize = 29.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.WorkOutline,
                    contentDescription = "My jobs",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(25.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SummaryItem(
                modifier = Modifier.weight(1f),
                label = "Total jobs",
                value = jobCount.toString(),
                accent = MaterialTheme.colorScheme.primary
            )

            SummaryItem(
                modifier = Modifier.weight(1f),
                label = "Pending",
                value = pendingCount.toString(),
                accent = ServexaOrange
            )

            SummaryItem(
                modifier = Modifier.weight(1f),
                label = "Accepted",
                value = acceptedCount.toString(),
                accent = ServexaGreen
            )
        }
    }
}

@Composable
private fun SummaryItem(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    accent: Color
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 12.dp, vertical = 13.dp)
    ) {
        Text(
            text = value,
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            color = accent
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun JobCard(
    job: Job,
    onClick: () -> Unit,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    onComplete: () -> Unit,
    onChatClick: () -> Unit = {}
) {
    val status = job.status

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(19.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(17.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Service",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = job.title.ifBlank { "Service request" },
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Customer: ${job.customerName.ifBlank { "Customer" }}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                JobStatusBadge(status = status)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = job.description.ifBlank {
                    "No description provided."
                },
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            Spacer(modifier = Modifier.height(13.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Estimated price",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = "₹${"%.0f".format(java.util.Locale("en", "IN"), job.price)}",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                TextButton(
                    onClick = onClick
                ) {
                    Text(
                        text = "Details",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.width(3.dp))

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "View details",
                        tint = ServexaPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            when {
                status.equals("Pending", ignoreCase = true) -> {
                    Spacer(modifier = Modifier.height(13.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onAccept,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Accept job")
                        }

                        Button(
                            onClick = onReject,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        ) {
                            Text("Decline")
                        }
                    }
                }

                status.equals("Accepted", ignoreCase = true) -> {
                    Spacer(modifier = Modifier.height(13.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onChatClick,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                contentColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Chat,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )

                            Spacer(modifier = Modifier.width(7.dp))

                            Text("Message")
                        }

                        Button(
                            onClick = onComplete,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = Color.White
                            )
                        ) {
                            Text("Complete job")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun JobStatusBadge(status: String) {
    val (label, background, foreground) = when {
        status.equals("Completed", ignoreCase = true) ->
            Triple("Completed", ServexaGreenLight, ServexaGreen)

        status.equals("Accepted", ignoreCase = true) ->
            Triple("Accepted", MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.primary)

        status.equals("Rejected", ignoreCase = true) ->
            Triple("Declined", MaterialTheme.colorScheme.errorContainer, MaterialTheme.colorScheme.onErrorContainer)

        else ->
            Triple("Pending", ServexaOrangeLight, ServexaOrange)
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = background
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(
                horizontal = 9.dp,
                vertical = 6.dp
            ),
            color = foreground,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
    }
}

@Composable
private fun MessageState(
    title: String,
    message: String,
    actionLabel: String?,
    onAction: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 30.dp,
                vertical = 42.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.WorkOutline,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(29.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            fontSize = 13.sp,
            lineHeight = 19.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (actionLabel != null) {
            Spacer(modifier = Modifier.height(14.dp))

            TextButton(onClick = onAction) {
                Text(
                    text = actionLabel,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun JobsBottomBar(
    onHomeClick: () -> Unit,
    onJobsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 28.dp,
                    vertical = 12.dp
                ),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            JobsBottomItem(
                icon = Icons.Default.Home,
                label = "Home",
                selected = false,
                onClick = onHomeClick
            )

            JobsBottomItem(
                icon = Icons.Default.WorkOutline,
                label = "Jobs",
                selected = true,
                onClick = onJobsClick
            )

            JobsBottomItem(
                icon = Icons.Default.Person,
                label = "Profile",
                selected = false,
                onClick = onProfileClick
            )
        }
    }
}

@Composable
fun JobsBottomItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(
                RoundedCornerShape(10.dp)
            )
            .clickable {
                onClick()
            }
            .padding(
                horizontal = 12.dp,
                vertical = 5.dp
            ),

        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = icon,
            contentDescription = label,

            tint =
                if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },

            modifier = Modifier.size(20.dp)
        )


        Spacer(
            modifier = Modifier.height(3.dp)
        )


        Text(
            text = label,

            fontSize = 10.sp,

            fontWeight =
                if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                },

            color =
                if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
        )
    }
}
