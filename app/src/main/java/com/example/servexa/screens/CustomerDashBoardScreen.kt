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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.servexa.viewModel.JobViewModel

private val ServexaPurple = Color(0xFF6750F5)
private val ServexaDarkPurple = Color(0xFF5141D9)
private val ServexaLightPurple = Color(0xFFEAE7FF)
private val ServexaBackground = Color(0xFFF8F7FC)

@Composable
fun CustomerDashboardScreen(
    jobViewModel: JobViewModel,
    userName: String = "there",
    onServiceClick: (String) -> Unit = {},
    onRequestsClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val jobs = jobViewModel.jobs.collectAsState()
    LaunchedEffect(Unit) {
        jobViewModel.getCustomerJobs()
    }
    val recentJobs = jobs.value.take(2)
    val services = listOf(
        "Electrician" to Icons.Default.Build,
        "Plumber" to Icons.Default.WaterDrop,
        "Carpenter" to Icons.Default.HomeRepairService,
        "Painter" to Icons.Default.Palette,
        "AC Technician" to Icons.Default.AcUnit,
        "Appliance Repair" to Icons.Default.Handyman
    )

    Scaffold(
        containerColor = ServexaBackground,

        bottomBar = {

            NavigationBar(
                containerColor = Color.White
            ) {

                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            imageVector = Icons.Default.HomeRepairService,
                            contentDescription = "Home"
                        )
                    },
                    label = {
                        Text("Home")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onRequestsClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Requests"
                        )
                    },
                    label = {
                        Text("Requests")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onProfileClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile"
                        )
                    },
                    label = {
                        Text("Profile")
                    }
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {



            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 32.dp,
                            bottomEnd = 32.dp
                        )
                    )
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                ServexaPurple,
                                ServexaDarkPurple
                            )
                        )
                    )
                    .padding(
                        horizontal = 24.dp,
                        vertical = 28.dp
                    )
            ) {

                Column {

                    Text(
                        text = "Hello, $userName 👋",
                        color = Color.White,
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )

                    Text(
                        text = "What service do you need today?",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 15.sp
                    )

                    Spacer(
                        modifier = Modifier.height(24.dp)
                    )

                    // Find Service Card

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onServiceClick("")
                            },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape)
                                    .background(ServexaLightPurple),
                                contentAlignment = Alignment.Center
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Handyman,
                                    contentDescription = null,
                                    tint = ServexaPurple,
                                    modifier = Modifier.size(25.dp)
                                )
                            }

                            Spacer(
                                modifier = Modifier.size(14.dp)
                            )

                            Column(
                                modifier = Modifier.weight(1f)
                            ) {

                                Text(
                                    text = "Find a Service",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF222222)
                                )

                                Spacer(
                                    modifier = Modifier.height(3.dp)
                                )

                                Text(
                                    text = "Find the right professional for your problem",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }
            }




            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 20.dp
                    )
            ) {

                Text(
                    text = "Popular Services",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF202020)
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )

                // Service Grid

                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(services) { service ->

                        CustomerServiceCard(
                            name = service.first,
                            icon = service.second,
                            onClick = {
                                onServiceClick(service.first)
                            }
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )



                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Recent Requests",
                        modifier = Modifier.weight(1f),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF202020)
                    )

                    Text(
                        text = "See all",
                        modifier = Modifier.clickable {
                            onRequestsClick()
                        },
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ServexaPurple
                    )
                }

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                // Empty state for now

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    )
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(CircleShape)
                                .background(ServexaLightPurple),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.AccessTime,
                                contentDescription = null,
                                tint = ServexaPurple,
                                modifier = Modifier.size(25.dp)
                            )
                        }

                        Spacer(
                            modifier = Modifier.height(10.dp)
                        )
                        if (recentJobs.isEmpty()) {

                            Text(
                                text = "No requests yet",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Your service requests will appear here",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        else {

                            recentJobs.forEach { job ->

                                CustomerRequestCard(
                                    service = job.title,
                                    description = job.description,
                                    providerName = job.providerName,
                                    price = job.price,
                                    status = job.status
                                )
                            }


                    }
                }
                }
            }
        }
    }
}


@Composable
private fun CustomerServiceCard(
    name: String,
    icon: ImageVector,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
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
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(ServexaLightPurple),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = name,
                    tint = ServexaPurple,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(
                modifier = Modifier.height(7.dp)
            )

            Text(
                text = name,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                maxLines = 1
            )
        }
    }
}
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//private fun CustomerDashboardScreenPreview() {
//    CustomerDashboardScreen(
//        userName = "Adarsh",
//        onServiceClick = {},
//        onRequestsClick = {},
//        onProfileClick = {}
//    )
//}