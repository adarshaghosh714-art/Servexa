package com.example.servexa.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.servexa.viewModel.AuthViewModel


private val ServexaPurple = Color(0xFF5B4AEF)
private val ServexaPurpleDark = Color(0xFF4335C7)
private val ServexaPurpleSoft = Color(0xFFF1EFFF)

private val ServexaBackground = Color(0xFFF8F8FB)
private val ServexaText = Color(0xFF18181B)
private val ServexaSecondaryText = Color(0xFF71717A)
private val ServexaBorder = Color(0xFFE4E4E7)


@Composable
fun RegisterScreen(
    viewModel: AuthViewModel,
    onLoginClick: () -> Unit = {},
    onRegisterSuccess: (String) -> Unit = {}
) {

    var registeredRole by remember {
        mutableStateOf("")
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLoggedIn) {

        if (uiState.isLoggedIn) {
            onRegisterSuccess(registeredRole)
        }
    }

    RegisterScreenContent(
        isLoading = uiState.isLoading,
        errorMessage = uiState.errorMessage,

        onRegisterClick = {
                name,
                email,
                password,
                confirmPassword,
                role ->

            registeredRole = role

            viewModel.register(
                name = name,
                email = email,
                password = password,
                confirmPassword = confirmPassword,
                role = role
            )
        },

        onLoginClick = onLoginClick
    )
}


@Composable
fun RegisterScreenContent(
    isLoading: Boolean,
    errorMessage: String?,
    onRegisterClick: (
        String,
        String,
        String,
        String,
        String
    ) -> Unit,
    onLoginClick: () -> Unit
) {

    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var confirmPasswordVisible by remember {
        mutableStateOf(false)
    }

    var selectedRole by remember {
        mutableStateOf("")
    }


    val bgColor = MaterialTheme.colorScheme.background
    val textColor = MaterialTheme.colorScheme.onBackground
    val secondaryText = MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = 24.dp,
                vertical = 28.dp
            )
    ) {

        // ------------------------------------------------
        // BRAND
        // ------------------------------------------------

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = ServexaPurple,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "S",
                    color = Color.White,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(
                modifier = Modifier.width(11.dp)
            )

            Text(
                text = "SERVEXA",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.5.sp,
                color = textColor
            )
        }


        Spacer(
            modifier = Modifier.height(38.dp)
        )


        // ------------------------------------------------
        // HEADER
        // ------------------------------------------------

        Text(
            text = "Create your account",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Set up your Servexa profile to get started.",
            fontSize = 15.sp,
            color = secondaryText
        )


        Spacer(
            modifier = Modifier.height(32.dp)
        )


        // ------------------------------------------------
        // PERSONAL DETAILS
        // ------------------------------------------------

        SectionTitle(
            text = "PERSONAL DETAILS"
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )


        ServexaTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = "Full name",
            icon = Icons.Default.Person,
            keyboardType = KeyboardType.Text
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        ServexaTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = "Email address",
            icon = Icons.Default.Email,
            keyboardType = KeyboardType.Email
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = {
                Text("Password")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            trailingIcon = {

                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {

                    Icon(
                        imageVector =
                            if (passwordVisible) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                        contentDescription = null
                    )
                }
            },
            visualTransformation =
                if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
            shape = RoundedCornerShape(12.dp),
            colors = ServexaTextFieldColors()
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            label = {
                Text("Confirm password")
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            },
            trailingIcon = {

                IconButton(
                    onClick = {
                        confirmPasswordVisible =
                            !confirmPasswordVisible
                    }
                ) {

                    Icon(
                        imageVector =
                            if (confirmPasswordVisible) {
                                Icons.Default.VisibilityOff
                            } else {
                                Icons.Default.Visibility
                            },
                        contentDescription = null
                    )
                }
            },
            visualTransformation =
                if (confirmPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
            isError = errorMessage == "Passwords do not match",
            shape = RoundedCornerShape(12.dp),
            colors = ServexaTextFieldColors()
        )


        if (errorMessage == "Passwords do not match") {

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = errorMessage,
                color = Color(0xFFDC2626),
                fontSize = 13.sp
            )
        }


        Spacer(
            modifier = Modifier.height(32.dp)
        )


        // ------------------------------------------------
        // ACCOUNT TYPE
        // ------------------------------------------------

        SectionTitle(
            text = "I WANT TO USE SERVEXA AS"
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )


        RoleCard(
            title = "Customer",
            description = "Find and hire trusted service providers.",
            icon = Icons.Default.Person,
            selected = selectedRole == "CUSTOMER",
            onClick = {
                selectedRole = "CUSTOMER"
            }
        )


        Spacer(
            modifier = Modifier.height(10.dp)
        )


        RoleCard(
            title = "Service Provider",
            description = "Offer your skills and get service requests.",
            icon = Icons.Default.Handyman,
            selected = selectedRole == "SERVICE_PROVIDER",
            onClick = {
                selectedRole = "SERVICE_PROVIDER"
            }
        )


        // ------------------------------------------------
        // ERROR
        // ------------------------------------------------

        if (
            errorMessage != null &&
            errorMessage != "Passwords do not match"
        ) {

            Spacer(
                modifier = Modifier.height(14.dp)
            )

            Text(
                text = errorMessage,
                color = Color(0xFFDC2626),
                fontSize = 13.sp
            )
        }


        Spacer(
            modifier = Modifier.height(26.dp)
        )


        // ------------------------------------------------
        // CREATE ACCOUNT
        // ------------------------------------------------

        Button(
            onClick = {

                onRegisterClick(
                    name,
                    email,
                    password,
                    confirmPassword,
                    selectedRole
                )
            },

            enabled = !isLoading,

            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),

            shape = RoundedCornerShape(12.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = ServexaPurple,
                disabledContainerColor = ServexaPurple.copy(
                    alpha = 0.55f
                )
            )
        ) {

            if (isLoading) {

                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )

            } else {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {

                    Text(
                        text = "Create account",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Icon(
                        imageVector = Icons.Default.Badge,
                        contentDescription = null,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }


        Spacer(
            modifier = Modifier.height(22.dp)
        )


        // ------------------------------------------------
        // LOGIN
        // ------------------------------------------------

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Already have an account?",
                fontSize = 14.sp,
                color = ServexaSecondaryText
            )

            TextButton(
                onClick = onLoginClick
            ) {

                Text(
                    text = "Log in",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ServexaPurple
                )
            }
        }


        Spacer(
            modifier = Modifier.height(12.dp)
        )
    }
}


// ========================================================
// SECTION TITLE
// ========================================================

@Composable
private fun SectionTitle(
    text: String
) {

    Text(
        text = text,
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}


// ========================================================
// TEXT FIELD
// ========================================================

@Composable
private fun ServexaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    keyboardType: KeyboardType
) {

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,

        modifier = Modifier.fillMaxWidth(),

        singleLine = true,

        label = {
            Text(label)
        },

        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },

        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
            keyboardType = keyboardType
        ),

        shape = RoundedCornerShape(12.dp),

        colors = ServexaTextFieldColors()
    )
}


// ========================================================
// TEXT FIELD COLORS
// ========================================================

@Composable
private fun ServexaTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedBorderColor = MaterialTheme.colorScheme.primary,
        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
        focusedLabelColor = MaterialTheme.colorScheme.primary,
        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
        cursorColor = MaterialTheme.colorScheme.primary,
        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        focusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant,
        focusedTextColor = MaterialTheme.colorScheme.onSurface,
        unfocusedTextColor = MaterialTheme.colorScheme.onSurface
    )


// ========================================================
// ROLE CARD
// ========================================================

@Composable
private fun RoleCard(
    title: String,
    description: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {

    Card(
        onClick = onClick,

        modifier = Modifier.fillMaxWidth(),

        shape = RoundedCornerShape(14.dp),

        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),

        border = BorderStroke(
            width = if (selected) 1.5.dp else 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outlineVariant
            }
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
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
                    .size(42.dp)
                    .background(
                        color = if (selected) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        },
                        shape = RoundedCornerShape(11.dp)
                    ),

                contentAlignment = Alignment.Center
            ) {

                Icon(
                    imageVector = icon,
                    contentDescription = null,

                    tint = if (selected) {
                        Color.White
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },

                    modifier = Modifier.size(21.dp)
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
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(
                    modifier = Modifier.height(3.dp)
                )

                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }


            if (selected) {

                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(50)
                        ),

                    contentAlignment = Alignment.Center
                ) {

                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }
    }
}