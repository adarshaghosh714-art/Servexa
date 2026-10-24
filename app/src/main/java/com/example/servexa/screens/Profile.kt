
package com.example.servexa.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.servexa.Setting.ThemeMode
import com.example.servexa.Setting.ThemePreferences

import com.example.servexa.viewModel.UserViewModel
import com.example.servexa.ui.theme.ServexaTheme
import kotlinx.coroutines.launch

// Brand accent colors stay consistent in both light and dark themes.
private val ServexaPurple = Color(0xFF5B4AEF)
private val ServexaGreen = Color(0xFF16A34A)
private val ServexaGreenSoft = Color(0xFFDCFCE7)

@Composable
private fun servexaBackground(): Color = MaterialTheme.colorScheme.background

@Composable
private fun servexaText(): Color = MaterialTheme.colorScheme.onBackground

@Composable
private fun servexaSecondaryText(): Color = MaterialTheme.colorScheme.onSurfaceVariant

@Composable
private fun servexaMutedText(): Color = MaterialTheme.colorScheme.outline

@Composable
private fun servexaDivider(): Color = MaterialTheme.colorScheme.outlineVariant

@Composable
private fun servexaPurpleSoft(): Color = MaterialTheme.colorScheme.secondaryContainer

@Composable
fun ProfileScreen(
    viewModel: UserViewModel,
    onHomeClick: () -> Unit,
    onJobsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onSwitchAccountClick: (String) -> Unit
) {
    val user by viewModel.user.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    // Theme preferences
    val context = LocalContext.current
    val themePreferences = remember(context) {
        ThemePreferences(context)
    }

    val themeMode by themePreferences.themeMode
        .collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)

    val coroutineScope = rememberCoroutineScope()

    var isEditing by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf("") }
    var editedServices by remember {
        mutableStateOf<List<String>>(emptyList())
    }
    var isSwitchingAccount by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getCurrentUser()
    }

    LaunchedEffect(user) {
        if (user != null) {
            editedName = user!!.name
            editedServices = user!!.services
        }
    }

    val currentRole = user?.currentRole ?: "CUSTOMER"

    val targetRole = if (currentRole == "SERVICE_PROVIDER") {
        "CUSTOMER"
    } else {
        "SERVICE_PROVIDER"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(servexaBackground())
    ) {
        ProfileTopHeader()

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            ProfileIdentity(
                name = user?.name?.ifBlank { "User" } ?: "User",
                role = currentRole
            )

            Spacer(modifier = Modifier.height(46.dp))

            // ACCOUNT DETAILS
            ProfileSectionLabel(text = "ACCOUNT")

            Spacer(modifier = Modifier.height(14.dp))

            if (isEditing) {
                EditNameField(
                    value = editedName,
                    onValueChange = { editedName = it }
                )
            } else {
                ProfileDetailRow(
                    title = "Personal information",
                    value = user?.name?.ifBlank { "Not provided" }
                        ?: "Not provided",
                    icon = Icons.Default.Person,
                    showArrow = false
                )
            }

            ProfileDivider()

            ProfileDetailRow(
                title = "Email address",
                value = user?.email ?: "Not available",
                icon = Icons.Default.Email,
                showArrow = false
            )

            ProfileDivider()

            ProfileDetailRow(
                title = "Active account",
                value = roleDisplayName(currentRole),
                icon = Icons.Default.Work,
                showArrow = false
            )

            Spacer(modifier = Modifier.height(46.dp))

            // SERVICES
            ProfileSectionLabel(text = "YOUR SERVICES")

            Spacer(modifier = Modifier.height(14.dp))

            if (isEditing) {
                EditableServices(
                    selectedServices = editedServices,
                    onServicesChange = { editedServices = it }
                )
            } else {
                ServicesDisplay(
                    services = user?.services.orEmpty()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // APPEARANCE / THEME SETTINGS
            ProfileSectionLabel(text = "APPEARANCE")

            Spacer(modifier = Modifier.height(14.dp))

            AppearanceSection(
                selectedMode = themeMode,
                onModeSelected = { selectedMode ->
                    coroutineScope.launch {
                        themePreferences.setThemeMode(selectedMode)
                    }
                }
            )

            Spacer(modifier = Modifier.height(42.dp))

            // EDIT / SAVE PROFILE
            if (isEditing) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TextButton(
                        onClick = {
                            editedName = user?.name.orEmpty()
                            editedServices = user?.services.orEmpty()
                            isEditing = false
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Cancel",
                            color = servexaSecondaryText()
                        )
                    }

                    Button(
                        onClick = {
                            val name = editedName.trim()

                            if (name.isNotBlank()) {
                                val existingRoles = user?.role
                                    ?.toMutableSet()
                                    ?: mutableSetOf(currentRole)

                                existingRoles.add(currentRole)

                                viewModel.updateUser(
                                    name = name,
                                    currentRole = currentRole,
                                    roles = existingRoles.toList()
                                )

                                viewModel.updateServices(editedServices)
                                isEditing = false
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ServexaPurple
                        )
                    ) {
                        Text(
                            text = "Save changes",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            } else {
                Button(
                    onClick = { isEditing = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ServexaPurple
                    )
                ) {
                    Text(
                        text = "Edit profile",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // SWITCH ACCOUNT
            ProfileSectionLabel(text = "SWITCH ACCOUNT")

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Switch between your Customer and Service Provider accounts.",
                fontSize = 13.sp,
                color = servexaSecondaryText()
            )

            Spacer(modifier = Modifier.height(14.dp))

            Button(
                onClick = {
                    if (!isSwitchingAccount && user != null) {
                        isSwitchingAccount = true

                        viewModel.switchAccountRole(targetRole) {
                            isSwitchingAccount = false
                            onSwitchAccountClick(targetRole)
                        }
                    }
                },
                enabled = !isSwitchingAccount && user != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = servexaPurpleSoft(),
                    contentColor = ServexaPurple
                )
            ) {
                Text(
                    text = when {
                        isSwitchingAccount -> "Switching account..."
                        targetRole == "CUSTOMER" -> "Switch to Customer"
                        else -> "Switch to Service Provider"
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (!errorMessage.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = errorMessage.orEmpty(),
                    fontSize = 13.sp,
                    color = Color(0xFFDC2626)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
        }

        ProfileBottomBar(
            onHomeClick = onHomeClick,
            onJobsClick = onJobsClick,
            onProfileClick = onProfileClick
        )
    }
}



@Composable
private fun AppearanceSection(
    selectedMode: ThemeMode,
    onModeSelected: (ThemeMode) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(
                width = 1.dp,
                color = servexaDivider(),
                shape = RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 14.dp, vertical = 8.dp)
    ) {
        ThemeMode.values().forEachIndexed { index, mode ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        onModeSelected(mode)
                    }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = when (mode) {
                            ThemeMode.SYSTEM -> "System default"
                            ThemeMode.LIGHT -> "Light"
                            ThemeMode.DARK -> "Dark"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = servexaText()
                    )

                    Spacer(modifier = Modifier.height(3.dp))

                    Text(
                        text = when (mode) {
                            ThemeMode.SYSTEM ->
                                "Follow your device settings"

                            ThemeMode.LIGHT ->
                                "Always use light appearance"

                            ThemeMode.DARK ->
                                "Always use dark appearance"
                        },
                        fontSize = 12.sp,
                        color = servexaSecondaryText()
                    )
                }

                RadioButton(
                    selected = selectedMode == mode,
                    onClick = {
                        onModeSelected(mode)
                    }
                )
            }

            if (index < ThemeMode.values().lastIndex) {
                ProfileDivider()
            }
        }
    }
}



@Composable
private fun ProfileTopHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp, vertical = 24.dp)
    ) {
        Text(
            text = "SERVEXA",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
            color = ServexaPurple
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Profile",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = servexaText()
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Manage your account and services",
            fontSize = 14.sp,
            color = servexaSecondaryText()
        )
    }
}



@Composable
private fun ProfileIdentity(
    name: String,
    role: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(78.dp)
                .background(servexaPurpleSoft(), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = name.firstOrNull()?.uppercase() ?: "U",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                color = ServexaPurple
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = name,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = servexaText()
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = roleDisplayName(role),
            fontSize = 14.sp,
            color = servexaSecondaryText()
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(ServexaGreenSoft)
                .padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(ServexaGreen, CircleShape)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "Active",
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = ServexaGreen
            )
        }
    }
}



@Composable
private fun ProfileSectionLabel(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp,
        color = servexaMutedText()
    )
}



@Composable
private fun ProfileDetailRow(
    title: String,
    value: String,
    icon: ImageVector,
    showArrow: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 17.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    servexaPurpleSoft(),
                    RoundedCornerShape(9.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = ServexaPurple,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 12.sp,
                color = servexaSecondaryText()
            )

            Spacer(modifier = Modifier.height(3.dp))

            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = servexaText()
            )
        }

        if (showArrow) {
            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = servexaMutedText(),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}


@Composable
private fun ProfileDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(servexaDivider())
    )
}



@Composable
private fun EditNameField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        label = { Text("Full name") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
        },
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = ServexaPurple,
            unfocusedBorderColor = servexaDivider(),
            focusedLabelColor = ServexaPurple,
            cursorColor = ServexaPurple
        )
    )
}



@Composable
private fun ServicesDisplay(services: List<String>) {
    if (services.isEmpty()) {
        Text(
            text = "No services added yet.",
            fontSize = 14.sp,
            color = servexaSecondaryText()
        )
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            services.chunked(2).forEach { rowServices ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowServices.forEach { service ->
                        ServiceTag(service = service)
                    }
                }
            }
        }
    }
}



@Composable
private fun ServiceTag(service: String) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .border(
                width = 1.dp,
                color = servexaDivider(),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 11.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Build,
            contentDescription = null,
            tint = ServexaPurple,
            modifier = Modifier.size(15.dp)
        )

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = service,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = servexaText()
        )
    }
}



@Composable
private fun EditableServices(
    selectedServices: List<String>,
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

    Column {
        availableServices.forEach { service ->
            val selected = selectedServices.contains(service)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onServicesChange(
                            if (selected) {
                                selectedServices - service
                            } else {
                                selectedServices + service
                            }
                        )
                    }
                    .padding(vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = selected,
                    onCheckedChange = { checked ->
                        onServicesChange(
                            if (checked) {
                                if (selected) {
                                    selectedServices
                                } else {
                                    selectedServices + service
                                }
                            } else {
                                selectedServices - service
                            }
                        )
                    }
                )

                Spacer(modifier = Modifier.width(6.dp))

                Text(
                    text = service,
                    fontSize = 14.sp,
                    color = servexaText()
                )
            }
        }
    }
}



private fun roleDisplayName(role: String): String {
    return when (role) {
        "SERVICE_PROVIDER" -> "Service Provider"
        "CUSTOMER" -> "Customer"
        else -> role
    }
}



@Composable
private fun ProfileBottomBar(
    onHomeClick: () -> Unit,
    onJobsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .border(width = 1.dp, color = servexaDivider())
            .navigationBarsPadding()
            .padding(horizontal = 40.dp, vertical = 10.dp),
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
            icon = Icons.Default.Work,
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
private fun ProfileBottomItem(
    icon: ImageVector,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (selected) ServexaPurple
            else servexaSecondaryText(),
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (selected) FontWeight.Bold
            else FontWeight.Medium,
            color = if (selected) ServexaPurple
            else servexaSecondaryText()
        )
    }
}



@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420"
)
@Composable
fun ProfileScreenPreview() {
    ServexaTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ProfileTopHeader()

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 28.dp)
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                ProfileIdentity(
                    name = "Adarsh Ghosh",
                    role = "SERVICE_PROVIDER"
                )

                Spacer(modifier = Modifier.height(46.dp))

                ProfileSectionLabel(text = "ACCOUNT")

                Spacer(modifier = Modifier.height(14.dp))

                ProfileDetailRow(
                    title = "Personal information",
                    value = "Adarsh Ghosh",
                    icon = Icons.Default.Person,
                    showArrow = false
                )

                ProfileDivider()

                ProfileDetailRow(
                    title = "Email address",
                    value = "adarsh@example.com",
                    icon = Icons.Default.Email,
                    showArrow = false
                )

                ProfileDivider()

                ProfileDetailRow(
                    title = "Active account",
                    value = "Service Provider",
                    icon = Icons.Default.Work,
                    showArrow = false
                )

                Spacer(modifier = Modifier.height(46.dp))

                ProfileSectionLabel(text = "YOUR SERVICES")

                Spacer(modifier = Modifier.height(14.dp))

                ServicesDisplay(
                    services = listOf(
                        "Electrician",
                        "AC Technician",
                        "Appliance Repair"
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                ProfileSectionLabel(text = "APPEARANCE")

                Spacer(modifier = Modifier.height(14.dp))

                AppearanceSection(
                    selectedMode = ThemeMode.SYSTEM,
                    onModeSelected = {}
                )

                Spacer(modifier = Modifier.height(42.dp))

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ServexaPurple
                    )
                ) {
                    Text(
                        text = "Edit profile",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                ProfileSectionLabel(text = "SWITCH ACCOUNT")

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Switch between your Customer and Service Provider accounts.",
                    fontSize = 13.sp,
                    color = servexaSecondaryText()
                )

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = servexaPurpleSoft(),
                        contentColor = ServexaPurple
                    )
                ) {
                    Text(
                        text = "Switch to Customer",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            ProfileBottomBar(
                onHomeClick = {},
                onJobsClick = {},
                onProfileClick = {}
            )
        }
    }
}
