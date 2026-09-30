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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.servexa.viewModel.JobViewModel

private val ServexaPurple = Color(0xFF6750F5)
private val ServexaLightPurple = Color(0xFFEAE7FF)
private val ServexaBackground = Color(0xFFF8F7FC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerRequestsScreen(
    viewModel: JobViewModel,
    onBackClick: () -> Unit = {}
) {
    val jobs = viewModel.jobs.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getCustomerJobs()
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

                    IconButton(
                        onClick = onBackClick
                    ) {

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

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Text(
                    text = "Your Service Requests",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(
                    modifier = Modifier.height(4.dp)
                )

                Text(
                    text = "Track the services you have requested",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )
            }



            items(jobs.value) { job ->

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


@Composable
fun CustomerRequestCard(
    service: String,
    description: String,
    providerName: String,
    price: Double,
    status: String
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
            modifier = Modifier.padding(18.dp)
        ) {



            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(ServexaLightPurple),

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = null,
                        tint = ServexaPurple,
                        modifier = Modifier.size(23.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.size(12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    Text(
                        text = service,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = description,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = providerName,
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }

                RequestStatusBadge(
                    status = status
                )
            }

            Spacer(
                modifier = Modifier.height(16.dp)
            )



            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(
                    modifier = Modifier.size(8.dp)
                )

                Column {

                    Text(
                        text = "Service Provider",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )

                    Text(
                        text = providerName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Budget",
                    modifier = Modifier.weight(1f),
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Text(
                    text = "₹${price.toInt()}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = ServexaPurple
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
                vertical = 5.dp
            ),

        verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(14.dp)
        )

        Spacer(
            modifier = Modifier.size(4.dp)
        )

        Text(
            text = status,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold,
            color = textColor
        )
    }
}


