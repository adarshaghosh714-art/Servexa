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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.servexa.viewModel.AuthViewModel
import com.example.servexa.viewModel.UserViewModel

private val ServexaBlue = Color(0xFF315FEA)
private val ServexaDarkBlue = Color(0xFF18264A)
private val ServexaPurple = Color(0xFF7657F5)
private val ServexaLightPurple = Color(0xFFE9E7FF)
private val ServexaOrange = Color(0xFFFF941F)
private val ServexaYellow = Color(0xFFFFB52E)
private val ServexaBackground = Color(0xFFF8F9FF)


// ======================================================
// DASHBOARD SCREEN
// ======================================================

@Composable
fun DashboardScreen(
    viewModel: UserViewModel,
    onHomeClick: () -> Unit,
    onJobsClick: () -> Unit,
    onProfileClick: () -> Unit,
) {

    val user by viewModel.user.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
    }
    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ServexaBackground)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
        ) {
            DashboardHeader(
                userName = user?.name ?: "User"
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 22.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                item {

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    OverviewCard()
                }

                item {
                    QuickActions()
                }

                item {
                    RecentJobs()
                }

                item {

                    Spacer(
                        modifier = Modifier.height(10.dp)
                    )
                }
            }

            DashboardBottomBar(
                onHomeClick = onHomeClick,
                onJobsClick = onJobsClick,
                onProfileClick = onProfileClick
            )
        }
    }
}


// ======================================================
// DASHBOARD HEADER
// ======================================================

@Composable
fun DashboardHeader(
    userName: String
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 22.dp,
                end = 22.dp,
                top = 24.dp,
                bottom = 18.dp
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = "Good morning, $userName 👋",
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = ServexaDarkBlue
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Here's your Servexa overview",
                fontSize = 14.sp,
                color = Color(0xFF7A8195)
            )
        }

        Box(
            modifier = Modifier
                .size(52.dp)
                .shadow(
                    elevation = 6.dp,
                    shape = CircleShape
                )
                .clip(CircleShape)
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            ServexaOrange,
                            ServexaYellow
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "S",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )
        }
    }
}


// ======================================================
// OVERVIEW CARD
// ======================================================

@Composable
fun OverviewCard() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(24.dp)
            )
            .clip(RoundedCornerShape(24.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        ServexaBlue,
                        ServexaPurple
                    )
                )
            )
            .padding(22.dp)
    ) {

        Column {

            Text(
                text = "TODAY'S OVERVIEW",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.3.sp,
                color = Color.White.copy(alpha = 0.75f)
            )

            Spacer(
                modifier = Modifier.height(18.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                DashboardStat(
                    number = "08",
                    label = "Total Jobs"
                )

                DashboardStat(
                    number = "03",
                    label = "Pending"
                )

                DashboardStat(
                    number = "05",
                    label = "Completed"
                )
            }
        }
    }
}


// ======================================================
// DASHBOARD STAT
// ======================================================

@Composable
fun DashboardStat(
    number: String,
    label: String
) {

    Column {

        Text(
            text = number,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.75f)
        )
    }
}


// ======================================================
// QUICK ACTIONS
// ======================================================

@Composable
fun QuickActions() {

    Column {

        Text(
            text = "Quick Actions",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = ServexaDarkBlue
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            ActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Add,
                title = "Add Job"
            )

            ActionCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Default.Group,
                title = "Customers"
            )
        }
    }
}


// ======================================================
// ACTION CARD
// ======================================================

@Composable
fun ActionCard(
    modifier: Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(ServexaLightPurple),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = ServexaPurple
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = ServexaDarkBlue
            )
        }
    }
}


// ======================================================
// RECENT JOBS
// ======================================================

@Composable
fun RecentJobs() {

    Column {

        Text(
            text = "Recent Jobs",
            fontSize = 20.sp,
            fontWeight = FontWeight.ExtraBold,
            color = ServexaDarkBlue
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        JobItem(
            title = "AC Repair",
            customer = "Rajesh Kumar",
            status = "PENDING"
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        JobItem(
            title = "Fan Installation",
            customer = "Amit Sharma",
            status = "COMPLETED"
        )
    }
}


// ======================================================
// JOB ITEM
// ======================================================

@Composable
fun JobItem(
    title: String,
    customer: String,
    status: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(ServexaLightPurple),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.Build,
                    contentDescription = "Job",
                    tint = ServexaPurple
                )
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = ServexaDarkBlue
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = customer,
                    fontSize = 13.sp,
                    color = Color(0xFF7A8195)
                )
            }

            Text(
                text = status,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (status == "PENDING") {
                    ServexaOrange
                } else {
                    Color(0xFF35A66F)
                }
            )
        }
    }
}


// ======================================================
// BOTTOM BAR
// ======================================================

@Composable
fun DashboardBottomBar(
    onProfileClick: () -> Unit,
    onHomeClick: () -> Unit,
    onJobsClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(
                horizontal = 24.dp,
                vertical = 14.dp
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        BottomItem(
            icon = Icons.Default.Home,
            label = "Home",
            selected = true,
            onClick = onHomeClick
        )

        BottomItem(
            icon = Icons.Default.Build,
            label = "Jobs",
            selected = false,
            onClick = onJobsClick
        )

        BottomItem(
            icon = Icons.Default.Person,
            label = "Profile",
            selected = false,
            onClick = onProfileClick
        )
    }
}


// ======================================================
// BOTTOM ITEM
// ======================================================

@Composable
fun BottomItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {

    Column(
        modifier = Modifier.clickable {
            onClick()
        },
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) {
                ServexaBlue
            } else {
                Color(0xFF8C93A8)
            },
            modifier = Modifier.size(24.dp)

        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            },
            color = if (selected) {
                ServexaBlue
            } else {
                Color(0xFF8C93A8)
            }
        )
    }
}


//// ======================================================
//// PREVIEW
//// ======================================================
//
//@Preview(
//    showBackground = true,
//    showSystemUi = true
//)
//@Composable
//fun DashboardScreenPreview() {
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(ServexaBackground)
//    ) {
//
//        DashboardHeader(
//            userName = "Adarsha"
//        )
//
//        LazyColumn(
//            modifier = Modifier
//                .weight(1f)
//                .padding(horizontal = 22.dp),
//            verticalArrangement = Arrangement.spacedBy(20.dp)
//        ) {
//
//            item {
//
//                Spacer(
//                    modifier = Modifier.height(4.dp)
//                )
//
//                OverviewCard()
//            }
//
//            item {
//                QuickActions()
//            }
//
//            item {
//                RecentJobs()
//            }
//
//            item {
//
//                Spacer(
//                    modifier = Modifier.height(10.dp)
//                )
//            }
//        }
//
//        DashboardBottomBar()
//    }
//}