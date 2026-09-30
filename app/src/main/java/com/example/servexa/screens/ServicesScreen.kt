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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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

data class ServiceOption(
    val name: String,
    val icon: ImageVector
)

@Composable
fun ServicesScreen(
    onSaveClick: (List<String>) -> Unit = {}
) {

    val selectedServices = remember {
        mutableStateListOf<String>()
    }

    val services = listOf(
        ServiceOption(
            "Electrician",
            Icons.Default.ElectricalServices
        ),
        ServiceOption(
            "Plumber",
            Icons.Default.WaterDrop
        ),
        ServiceOption(
            "Carpenter",
            Icons.Default.Handyman
        ),
        ServiceOption(
            "Painter",
            Icons.Default.Palette
        ),
        ServiceOption(
            "AC Technician",
            Icons.Default.HomeRepairService
        ),
        ServiceOption(
            "Appliance Repair",
            Icons.Default.Handyman
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7FC))
    ) {

        ServicesHeader()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Select your services",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF202124)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Choose all the services you provide.",
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(20.dp))

            services.forEach { service ->

                ServiceOptionCard(
                    service = service,
                    selected = selectedServices.contains(service.name),
                    onClick = {

                        if (selectedServices.contains(service.name)) {
                            selectedServices.remove(service.name)
                        } else {
                            selectedServices.add(service.name)
                        }
                    }
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    onSaveClick(selectedServices.toList())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5B5FEF)
                ),
                enabled = selectedServices.isNotEmpty()
            ) {

                Text(
                    text = "Save Services",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Composable
fun ServicesHeader() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
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
                .padding(24.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(
                        Color.White.copy(alpha = 0.2f)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = Icons.Default.HomeRepairService,
                    contentDescription = "Services",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "My Services",
                color = Color.White,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Composable
fun ServiceOptionCard(
    service: ServiceOption,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected)
                Color(0xFFE9E9FF)
            else
                Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (selected)
                            Color(0xFF5B5FEF)
                        else
                            Color(0xFFEDEDF8)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = service.icon,
                    contentDescription = service.name,
                    tint = if (selected)
                        Color.White
                    else
                        Color(0xFF5B5FEF)
                )
            }

            Spacer(modifier = Modifier.size(14.dp))

            Text(
                text = service.name,
                modifier = Modifier.weight(1f),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF202124)
            )

            if (selected) {

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF5B5FEF)),
                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ServicesScreenPreview() {

    ServicesScreen()
}