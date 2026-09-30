package com.example.servexa.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.servexa.viewModel.UserViewModel
import com.google.firebase.firestore.remote.WatchChange

private val ServexaBlue = Color(0xFF315FEA)
private val ServexaDarkBlue = Color(0xFF18264A)
private val ServexaPurple = Color(0xFF7657F5)
private val ServexaLightPurple = Color(0xFFE9E7FF)
private val ServexaBackground = Color(0xFFF8F9FF)


@Composable
fun ProfileScreen(
    viewModel: UserViewModel,
    onHomeClick: () -> Unit,
    onJobsClick: () -> Unit,
    onProfileClick: () -> Unit,
) {


    val user by viewModel.user.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
    }
    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf("") }
    var editedRole by remember { mutableStateOf("") }
    var editedServices by remember {
        mutableStateOf<List<String>>(emptyList())
    }



    LaunchedEffect(user) {
        if (user != null) {
            editedName = user!!.name
            editedRole = user!!.currentRole
            editedServices = user!!.services
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ServexaBackground)
    ) {

        ProfileHeader(userName = user?.name ?: "User")

        Column(modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
        ) {

            Spacer(
                modifier = Modifier.height(20.dp)
            )


            ProfileInformation(
                userName = editedName,
                userEmail = user?.email ?: "No email",
                userRole = editedRole,
                isEditing = isEditing,
                onNameChange = {
                    editedName = it
                },
                onRoleChange = {
                    editedRole = it
                }
            )

            if (user?.services?.isNotEmpty() == true) {

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                ProfileServices(
                    services = editedServices,
                    isEditing = isEditing,
                    onServicesChange = {
                        editedServices = it
                    }
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            EditProfileButton(
                isEditing = isEditing,
                onClick = {

                    if (isEditing) {

                        viewModel.updateUser(
                            name = editedName,
                            currentRole = editedRole,
                            roles = listOf(editedRole)
                        )

                        viewModel.updateServices(
                            editedServices
                        )

                        isEditing = false

                    } else {

                        isEditing = true
                    }
                })
        }

        ProfileBottomBar(
            onHomeClick = onHomeClick,
            onJobsClick = onJobsClick,
            onProfileClick = onProfileClick
        )
    }
}


@Composable
fun ProfileHeader(userName: String) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 10.dp,
                shape = RoundedCornerShape(
                    bottomStart = 30.dp,
                    bottomEnd = 30.dp
                )
            )
            .clip(
                RoundedCornerShape(
                    bottomStart = 30.dp,
                    bottomEnd = 30.dp
                )
            )
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        ServexaBlue,
                        ServexaPurple
                    )
                )
            )
            .padding(
                top = 30.dp,
                bottom = 30.dp
            )
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Avatar
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = userName.firstOrNull()?.uppercase() ?: "U",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = ServexaPurple
                )
            }

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = userName,
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Servexa User",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.75f)
            )
        }
    }
}


@Composable
fun ProfileInformation(
    userName: String,
    userEmail: String,
    userRole: String,
    isEditing: Boolean,
    onNameChange: (String) -> Unit,
    onRoleChange: (String) -> Unit
) {
    var roleExpanded by remember {
        mutableStateOf(false)
    }

    val roles = listOf(
        "CUSTOMER",
        "SERVICE_PROVIDER"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Profile Information",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            // NAME
            if (isEditing) {

                OutlinedTextField(
                    value = userName,
                    onValueChange = onNameChange,
                    label = {
                        Text("Full Name")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

            } else {

                ProfileInfoCard(
                    icon = Icons.Default.Person,
                    title = "Full Name",
                    value = userName
                )
            }

            // EMAIL
            ProfileInfoCard(
                icon = Icons.Default.Email,
                title = "Email Address",
                value = userEmail
            )

            // ROLE
            if (isEditing) {

                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    OutlinedTextField(
                        value = userRole,
                        onValueChange = {},
                        readOnly = true,
                        label = {
                            Text("Account Type")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Build,
                                contentDescription = "Select Role"
                            )
                        }
                    )

                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable {
                                roleExpanded = true
                            }
                    )

                    DropdownMenu(
                        expanded = roleExpanded,
                        onDismissRequest = {
                            roleExpanded = false
                        }
                    ) {
                        roles.forEach { role ->

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        if (role == "SERVICE_PROVIDER")
                                            "Service Provider"
                                        else
                                            "Customer"
                                    )
                                },
                                onClick = {
                                    onRoleChange(role)
                                    roleExpanded = false
                                }
                            )
                        }
                    }
                }

            } else {

                ProfileInfoCard(
                    icon = Icons.Default.Build,
                    title = "Account Type",
                    value = if (userRole == "SERVICE_PROVIDER")
                        "Service Provider"
                    else
                        "Customer"
                )
            }
        }
    }
}

@Composable
fun ProfileServices(
    services: List<String>,
    isEditing: Boolean,
    onServicesChange: (List<String>) -> Unit
) {
    val availableServices = listOf(
        "Electrician",
        "Plumber",
        "Carpenter",
        "Painter",
        "AC Technician",
        "Appliance Repair"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Services",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            if (isEditing) {

                availableServices.forEach { service ->

                    val isSelected = services.contains(service)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                val updatedServices =
                                    if (isSelected) {
                                        services - service
                                    } else {
                                        services + service
                                    }

                                onServicesChange(updatedServices)
                            },

                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        androidx.compose.material3.Checkbox(
                            checked = isSelected,

                            onCheckedChange = {

                                val updatedServices =
                                    if (isSelected) {
                                        services - service
                                    } else {
                                        services + service
                                    }

                                onServicesChange(updatedServices)
                            }
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        Text(
                            text = service,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

            } else {

                services.forEach { service ->

                    Row(
                        modifier = Modifier.fillMaxWidth(),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Default.Build,
                            contentDescription = null,
                            tint = ServexaPurple,
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(12.dp)
                        )

                        Text(
                            text = service,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileInfoCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
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
                    imageVector = icon,
                    contentDescription = title,
                    tint = ServexaPurple,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column {

                Text(
                    text = title,
                    fontSize = 12.sp,
                    color = Color(0xFF7A8195)
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = value,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ServexaDarkBlue
                )
            }
        }
    }
}


@Composable
fun EditProfileButton(
    isEditing: Boolean,
    onClick: () -> Unit
) {

    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {

        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = if (isEditing) {
                "Save profile"
            } else {
                "Edit profile"
            }
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = if (isEditing) {
                "SAVE CHANGES"
            } else {
                "EDIT PROFILE"
            },
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun ProfileBottomBar(
    onHomeClick: () -> Unit,
    onJobsClick: () -> Unit,
    onProfileClick: () -> Unit,
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

        ProfileBottomItem(
            icon = Icons.Default.Home,
            label = "Home",
            selected = false,
            onClick = onHomeClick

        )

        ProfileBottomItem(
            icon = Icons.Default.Build,
            label = "Jobs",
            selected = false,
            onClick = onJobsClick
        )

        ProfileBottomItem(
            icon = Icons.Default.Person,
            label = "Profile",
            selected = true,
            onClick = onProfileClick
        )
    }
}


@Composable
fun ProfileBottomItem(
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



