
package com.example.servexa.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.servexa.viewModel.AuthViewModel

private val ServexaBlue = Color(0xFF315FEA)
private val ServexaDarkBlue = Color(0xFF18264A)
private val ServexaPurple = Color(0xFF7657F5)
private val ServexaOrange = Color(0xFFFF941F)
private val ServexaYellow = Color(0xFFFFB52E)
private val ServexaFieldBackground = Color(0xFFF8F9FF)

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onLoginSuccess()
        }
    }

    val bgColor = MaterialTheme.colorScheme.background
    val textColor = MaterialTheme.colorScheme.onBackground
    val secondaryTextColor = MaterialTheme.colorScheme.onSurfaceVariant
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    val outlineVariant = MaterialTheme.colorScheme.outlineVariant
    val primaryColor = MaterialTheme.colorScheme.primary

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
    ) {
        // Top background wave
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(330.dp)
        ) {
            val width = size.width
            val height = size.height

            drawRect(
                color = ServexaBlue,
                size = Size(width, height)
            )

            val lightPath = Path().apply {
                moveTo(0f, height * 0.18f)
                cubicTo(
                    width * 0.30f, height * 0.05f,
                    width * 0.65f, height * 0.20f,
                    width, height * 0.05f
                )
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }

            drawPath(
                path = lightPath,
                color = Color(0xFFB7C8FF).copy(alpha = 0.5f)
            )

            val whitePath = Path().apply {
                moveTo(0f, height * 0.35f)
                cubicTo(
                    width * 0.35f, height * 0.15f,
                    width * 0.55f, height * 0.55f,
                    width, height * 0.45f
                )
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }

            drawPath(
                path = whitePath,
                color = bgColor
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(
                    start = 26.dp,
                    end = 26.dp,
                    top = 46.dp,
                    bottom = 30.dp
                )
        ) {
            // Servexa branding
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .shadow(8.dp, CircleShape)
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
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Servexa",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textColor
                    )

                    Text(
                        text = "FIELD SERVICE PLATFORM",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp,
                        color = primaryColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(35.dp))

            // Illustration
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(165.dp)
                        .clip(
                            RoundedCornerShape(
                                topStart = 80.dp,
                                topEnd = 80.dp,
                                bottomStart = 55.dp,
                                bottomEnd = 80.dp
                            )
                        )
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    ServexaPurple,
                                    ServexaBlue
                                )
                            )
                        )
                )

                Box(
                    modifier = Modifier
                        .size(75.dp)
                        .align(Alignment.Center)
                        .clip(RoundedCornerShape(38.dp))
                        .background(Color(0xFF24304F)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "S",
                        fontSize = 34.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.BottomEnd)
                        .clip(CircleShape)
                        .background(ServexaOrange),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Build,
                        contentDescription = "Tools",
                        tint = Color.White,
                        modifier = Modifier.size(27.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .align(Alignment.TopStart)
                        .clip(CircleShape)
                        .background(Color(0xFFFFA027)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White,
                        modifier = Modifier.size(25.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Time",
                        tint = primaryColor,
                        modifier = Modifier.size(29.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Welcome back 👋",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Sign in to continue your journey",
                fontSize = 15.sp,
                color = secondaryTextColor
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Email field
            Text(
                text = "Email Address",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Enter your email",
                        color = secondaryTextColor
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = "Email",
                        tint = primaryColor
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = outlineVariant,
                    focusedContainerColor = surfaceVariant,
                    unfocusedContainerColor = surfaceVariant,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Password field
            Text(
                text = "Password",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Enter your password",
                        color = secondaryTextColor
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Password",
                        tint = primaryColor
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Default.VisibilityOff
                        } else {
                            Icons.Default.Visibility
                        },
                        contentDescription = if (passwordVisible) {
                            "Hide password"
                        } else {
                            "Show password"
                        },
                        tint = secondaryTextColor,
                        modifier = Modifier.clickable {
                            passwordVisible = !passwordVisible
                        }
                    )
                },
                visualTransformation = if (passwordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = outlineVariant,
                    focusedContainerColor = surfaceVariant,
                    unfocusedContainerColor = surfaceVariant,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                )
            )





            Spacer(modifier = Modifier.height(24.dp))

            // Login button: text and arrow are centered together.
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(30.dp)
                    )
                    .clip(RoundedCornerShape(30.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                ServexaOrange,
                                ServexaYellow
                            )
                        )
                    )
                    .clickable(enabled = !uiState.isLoading) {
                        viewModel.login(
                            email = email.trim(),
                            password = password
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.5.dp
                    )
                } else {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LOGIN",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Login",
                            tint = Color.White,
                            modifier = Modifier.size(23.dp)
                        )
                    }
                }
            }

            // Login error
            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Sign-up navigation
            Row(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable {
                        onSignUpClick()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    fontSize = 14.sp,
                    color = Color(0xFF70778B)
                )

                Text(
                    text = "Sign up →",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ServexaBlue
                )
            }

            Spacer(modifier = Modifier.height(25.dp))
        }

        // Bottom decorative waves
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .align(Alignment.BottomCenter)
        ) {
            val width = size.width
            val height = size.height

            val purplePath = Path().apply {
                moveTo(0f, height * 0.30f)
                cubicTo(
                    width * 0.25f, height * 0.05f,
                    width * 0.30f, height * 0.85f,
                    width * 0.65f, height * 0.72f
                )
                cubicTo(
                    width * 0.82f, height * 0.65f,
                    width * 0.90f, height * 0.40f,
                    width, height * 0.52f
                )
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }

            drawPath(
                path = purplePath,
                color = ServexaPurple
            )

            val bluePath = Path().apply {
                moveTo(0f, height * 0.62f)
                cubicTo(
                    width * 0.25f, height * 0.30f,
                    width * 0.45f, height * 0.95f,
                    width * 0.72f, height * 0.78f
                )
                cubicTo(
                    width * 0.86f, height * 0.70f,
                    width * 0.94f, height * 0.70f,
                    width, height * 0.80f
                )
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }

            drawPath(
                path = bluePath,
                color = ServexaBlue
            )

            for (row in 0..2) {
                for (column in 0..2) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.25f),
                        radius = 3f,
                        center = Offset(
                            x = 25f + column * 14f,
                            y = height - 25f - row * 14f
                        )
                    )
                }
            }
        }
    }
}
