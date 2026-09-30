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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.servexa.model.Job
import com.example.servexa.viewModel.JobViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobsScreen(
    viewModel: JobViewModel,

    onJobClick: (Job) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onJobsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {

    var searchText by remember {
        mutableStateOf("")
    }
    val jobs by viewModel.jobs.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()


    val filteredJobs = jobs.filter {
        it.title.contains(searchText, ignoreCase = true) ||
                it.customerName.contains(searchText, ignoreCase = true)
    }
    LaunchedEffect(Unit) {
        viewModel.getJobs()
    }

    Scaffold(

        bottomBar = {
            JobsBottomBar(
                onHomeClick = onHomeClick,
                onJobsClick = onJobsClick,
                onProfileClick = onProfileClick
            )
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF7F7FC)),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            item {

                JobsHeader(
                    jobCount = jobs.size
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    placeholder = {
                        Text("Search jobs...")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (errorMessage != null) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Something went wrong")
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = errorMessage ?: "Failed to load Jobs")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = {
                        viewModel.clearError()
                        viewModel.getJobs()
                    }) {
                        Text("Retry")
                    }

                }
            } else if (filteredJobs.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (searchText.isEmpty()) {
                                "No jobs yet"
                            } else {
                                "No jobs found"
                            }
                        )
                    }
                }
            } else {

                items(filteredJobs) { job ->

                    JobCard(

                        job = job,

                        onClick = {
                            onJobClick(job)
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}


@Composable
fun JobsHeader(
    jobCount: Int
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp)
            .clip(
                RoundedCornerShape(
                    bottomStart = 32.dp,
                    bottomEnd = 32.dp
                )
            )
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF5B5FEF),
                        Color(0xFF7B4DFF)
                    )
                )
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 24.dp,
                    vertical = 28.dp
                ),
            verticalArrangement = Arrangement.Center
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = "Jobs",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {

                    Text(
                        text = "My Jobs",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "$jobCount jobs available",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}


@Composable
fun JobCard(
    job: Job,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        onClick = onClick
    ) {

        Column(
            modifier = Modifier.padding(18.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = job.title,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF202124)
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = job.customerName,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                JobStatusBadge(
                    status = job.status
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = job.description,
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "₹${job.price.toInt()}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5B5FEF)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextButton(
                        onClick = onClick
                    ) {

                        Text(
                            text = "View",
                            color = Color(0xFF5B5FEF),
                            fontWeight = FontWeight.Bold
                        )

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "View Job",
                            tint = Color(0xFF5B5FEF),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun JobStatusBadge(
    status: String
) {

    val backgroundColor = when (status) {
        "Completed" -> Color(0xFFE5F8EC)
        "In Progress" -> Color(0xFFFFF1D6)
        else -> Color(0xFFE8E9FF)
    }

    val textColor = when (status) {
        "Completed" -> Color(0xFF1B8A4B)
        "In Progress" -> Color(0xFFD38300)
        else -> Color(0xFF5558C9)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .padding(
                horizontal = 12.dp,
                vertical = 6.dp
            )
    ) {

        Text(
            text = status,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun JobsBottomBar(
    onHomeClick: () -> Unit,
    onJobsClick: () -> Unit,
    onProfileClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        JobsBottomItem(
            icon = Icons.Default.Home,
            label = "Home",
            selected = false,
            onClick = onHomeClick
        )

        JobsBottomItem(
            icon = Icons.Default.Build,
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


@Composable
fun JobsBottomItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected)
                    Color(0xFFE9E9FF)
                else
                    Color.Transparent
            )
            .padding(
                horizontal = 20.dp,
                vertical = 6.dp
            )
            .clickable {
                onClick()
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected)
                Color(0xFF5B5FEF)
            else
                Color.Gray
        )

        Text(
            text = label,
            fontSize = 12.sp,
            color = if (selected)
                Color(0xFF5B5FEF)
            else
                Color.Gray
        )
    }
}


//@Preview(
//    showBackground = true,
//    showSystemUi = true
//)
//@Composable
//fun JobsScreenPreview() {
//
//    JobsScreen(viewModel = JobViewModel)
//}