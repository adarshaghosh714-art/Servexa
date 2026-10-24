package com.example.servexa.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.servexa.model.ServiceOption
import com.example.servexa.viewModel.ProviderViewModel
import com.example.servexa.viewModel.UserViewModel

private val ServexaPurple = Color(0xFF6C63FF)
private val ServexaDarkPurple = Color(0xFF4F46C7)
private val ServexaLightPurple = Color(0xFFF0EEFF)
private val ServexaBackground = Color(0xFFF7F7FC)
private val ServexaText = Color(0xFF202124)
private val ServexaGray = Color(0xFF777777)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostJobScreen(
    initialService: String = "",
    userViewModel: UserViewModel,
    providerViewModel: ProviderViewModel,
    onBackClick: () -> Unit = {},
    onPostJobClick: (
        service: String,
        description: String,
        budget: Double,
        providerId: String,
        providerName: String
    ) -> Unit = { _, _, _, _, _ -> }
) {

    var selectedService by remember {
        mutableStateOf(initialService)
    }

    var description by remember {
        mutableStateOf("")
    }

    var budget by remember {
        mutableStateOf("")
    }
    var selectedProvider by remember { mutableStateOf<String?>(null) }

    val providers by providerViewModel.providers.collectAsState()

    val users by userViewModel.user.collectAsState()
    LaunchedEffect(selectedService) {
        if (selectedService.isNotEmpty()) {
            providerViewModel.getProviderByService(selectedService)
        } else {
            providerViewModel.clearProviders()
        }
    }
    LaunchedEffect(Unit) {
        userViewModel.getCurrentUser()
    }

    val services = listOf(
        ServiceOption(
            name = "Electrician",
            icon = Icons.Default.ElectricalServices
        ),
        ServiceOption(
            name = "Plumber",
            icon = Icons.Default.Plumbing
        ),
        ServiceOption(
            name = "Carpenter",
            icon = Icons.Default.HomeRepairService
        ),
        ServiceOption(
            name = "Painter",
            icon = Icons.Default.Palette
        ),
        ServiceOption(
            name = "AC Technician",
            icon = Icons.Default.Air
        ),
        ServiceOption(
            name = "Appliance Repair",
            icon = Icons.Default.Build
        )
    )

    val isFormValid =
        selectedService.isNotEmpty() &&
                description.isNotBlank() &&
                budget.toDoubleOrNull() != null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Post a Job",
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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ServexaPurple,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {

            Text(
                text = "What do you need help with?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = "Choose a service and tell us what you need.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )



            Text(
                text = "Select Service",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = ServexaText
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    ServiceCard(
                        service = services[0],
                        selected = selectedService == services[0].name,
                        onClick = {
                            selectedService = services[0].name
                        },
                        modifier = Modifier.weight(1f)
                    )

                    ServiceCard(
                        service = services[1],
                        selected = selectedService == services[1].name,
                        onClick = {
                            selectedService = services[1].name
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    ServiceCard(
                        service = services[2],
                        selected = selectedService == services[2].name,
                        onClick = {
                            selectedService = services[2].name
                        },
                        modifier = Modifier.weight(1f)
                    )

                    ServiceCard(
                        service = services[3],
                        selected = selectedService == services[3].name,
                        onClick = {
                            selectedService = services[3].name
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    ServiceCard(
                        service = services[4],
                        selected = selectedService == services[4].name,
                        onClick = {
                            selectedService = services[4].name
                        },
                        modifier = Modifier.weight(1f)
                    )

                    ServiceCard(
                        service = services[5],
                        selected = selectedService == services[5].name,
                        onClick = {
                            selectedService = services[5].name
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )



            Text(
                text = "Describe your problem",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                placeholder = {
                    Text(
                        text = "Example: My bedroom fan is not working...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                shape = RoundedCornerShape(16.dp),
                maxLines = 6,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )



            Text(
                text = "Your Budget",
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            OutlinedTextField(
                value = budget,
                onValueChange = { newValue ->

                    if (
                        newValue.isEmpty() ||
                        newValue.matches(Regex("^\\d*\\.?\\d*$"))
                    ) {
                        budget = newValue
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Enter your maximum budget",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                },
                prefix = {
                    Text(
                        text = "₹ ",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedContainerColor = MaterialTheme.colorScheme.surface,
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedTextColor = MaterialTheme.colorScheme.onSurface,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurface
                )
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )



            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(46.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }

                        Spacer(
                            modifier = Modifier.width(14.dp)
                        )

                        Column {

                            Text(
                                text = "Select a Provider",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = if (selectedService.isEmpty()) {
                                    "Select a service first"
                                } else {
                                    "${providers.size} provider(s) found"
                                },
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    if (selectedService.isEmpty()) {

                        Text(
                            text = "Choose a service to find providers.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                    } else if (providers.isEmpty()) {

                        Text(
                            text = "No providers found for $selectedService.",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                    } else {

                        providers.forEach { provider ->

                            Card(
                                onClick = {
                                    selectedProvider = provider.uid
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor =
                                        if (selectedProvider == provider.uid) {
                                            MaterialTheme.colorScheme.surfaceVariant
                                        } else {
                                            MaterialTheme.colorScheme.surface
                                        }
                                ),
                                border =
                                    if (selectedProvider == provider.uid) {
                                        androidx.compose.foundation.BorderStroke(
                                            2.dp,
                                            MaterialTheme.colorScheme.primary
                                        )
                                    } else {
                                        BorderStroke(
                                            1.dp,
                                            MaterialTheme.colorScheme.outlineVariant
                                        )
                                    }
                            ) {

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {

                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(
                                                color = if (selectedProvider == provider.uid) {
                                                    MaterialTheme.colorScheme.primary
                                                } else {
                                                    MaterialTheme.colorScheme.surfaceVariant
                                                },
                                                shape = RoundedCornerShape(12.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {

                                        Icon(
                                            imageVector = Icons.Default.Person,
                                            contentDescription = null,
                                            tint = if (selectedProvider == provider.uid) {
                                                Color.White
                                            } else {
                                                MaterialTheme.colorScheme.primary
                                            }
                                        )
                                    }

                                    Spacer(
                                        modifier = Modifier.width(12.dp)
                                    )

                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {

                                        Text(
                                            text = provider.name,
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Spacer(
                                            modifier = Modifier.height(3.dp)
                                        )

                                        Text(
                                            text = provider.email,
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }

                                    Text(
                                        text =
                                            if (selectedProvider == provider.uid) {
                                                "Selected"
                                            } else {
                                                "Select"
                                            },
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            Spacer(
                                modifier = Modifier.height(10.dp)
                            )
                        }
                    }
                }
            }
            Spacer(
                modifier = Modifier.height(28.dp)
            )



            Button(
                onClick = {

                    val budgetValue =
                        budget.toDoubleOrNull()

                    val selectedProviderData =
                        providers.find {
                            it.uid == selectedProvider
                        }

                    if (
                        selectedService.isNotEmpty() &&
                        description.isNotBlank() &&
                        budgetValue != null &&
                        selectedProviderData != null
                    ) {
                        onPostJobClick(
                            selectedService,
                            description.trim(),
                            budgetValue,
                            selectedProviderData.uid,
                            selectedProviderData.name
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                enabled = selectedService.isNotEmpty() &&
                        description.isNotBlank() &&
                        budget.toDoubleOrNull() != null &&
                        selectedProvider != null,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f)
                )
            ) {

                Text(
                    text = "Post Request",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "You can review your request before confirming.",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )
        }
    }
}

@Composable
private fun ServiceCard(
    service: ServiceOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        onClick = onClick,
        modifier = modifier
            .height(120.dp)
            .then(
                if (selected) {
                    Modifier.border(
                        width = 2.dp,
                        color = ServexaPurple,
                        shape = RoundedCornerShape(18.dp)
                    )
                } else {
                    Modifier
                }
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (selected) 0.dp else 2.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                imageVector = service.icon,
                contentDescription = service.name,
                modifier = Modifier.size(30.dp),
                tint = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = service.name,
                fontSize = 14.sp,
                fontWeight = if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                },
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}