package com.example.servexa.navigation

import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.servexa.model.Job
import com.example.servexa.repository.AuthRepository
import com.example.servexa.repository.ChatRepository
import com.example.servexa.repository.JobRepository
import com.example.servexa.repository.ProviderRepository
import com.example.servexa.repository.UserRepository
import com.example.servexa.screens.ChatScreen
import com.example.servexa.screens.ConversationsScreen
import com.example.servexa.screens.CustomerDashboardScreen
import com.example.servexa.screens.CustomerRequestsScreen
import com.example.servexa.screens.DashboardScreen
import com.example.servexa.screens.JobsScreen
import com.example.servexa.screens.LoginScreen
import com.example.servexa.screens.PostJobScreen
import com.example.servexa.screens.ProfileScreen
import com.example.servexa.screens.RegisterScreen
import com.example.servexa.screens.ServicesScreen
import com.example.servexa.viewModel.AuthViewModel
import com.example.servexa.viewModel.AuthViewModelFactory
import com.example.servexa.viewModel.ChatViewModel
import com.example.servexa.viewModel.ChatViewModelFactory
import com.example.servexa.viewModel.JobViewModel
import com.example.servexa.viewModel.JobViewModelFactory
import com.example.servexa.viewModel.ProviderViewModel
import com.example.servexa.viewModel.ProviderViewModelFactory
import com.example.servexa.viewModel.UserViewModel
import com.example.servexa.viewModel.UserViewModelFactory

@Composable
fun ServexaNavGraph(
    navController: NavHostController
) {

    // AUTH VIEWMODEL
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(
            AuthRepository(),
            UserRepository()
        )
    )

    // SHARED CHAT VIEWMODEL
    val chatViewModel: ChatViewModel = viewModel(
        factory = ChatViewModelFactory(
            ChatRepository()
        )
    )

    // USER VIEWMODEL
    val userViewModel: UserViewModel = viewModel(
        factory = UserViewModelFactory(
            UserRepository()
        )
    )

    // PROVIDER VIEWMODEL
    val providerViewModel: ProviderViewModel = viewModel(
        factory = ProviderViewModelFactory(
            ProviderRepository()
        )
    )

    // JOB VIEWMODEL
    val jobViewModel: JobViewModel = viewModel(
        factory = JobViewModelFactory(
            JobRepository()
        )
    )

    var startDestination by remember {
        mutableStateOf<String?>(null)
    }

    LaunchedEffect(Unit) {
        userViewModel.checkLoginStatus { user ->
            startDestination = when (user?.currentRole) {
                "CUSTOMER" ->
                    Screen.CustomerDashBoardScreen.route

                "SERVICE_PROVIDER" ->
                    Screen.Dashboard.route

                else ->
                    Screen.Login.route
            }
        }
    }

    // Helper for main tab navigation to avoid duplicate screens and huge backstack build-up
    fun navigateToTab(route: String) {
        if (navController.currentDestination?.route == route) return
        navController.navigate(route) {
            val startId = navController.graph.startDestinationId
            if (startId != 0) {
                popUpTo(startId) {
                    saveState = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    // Helper to navigate after auth or setup, clearing auth screens from backstack
    fun navigateMain(destination: String) {
        navController.navigate(destination) {
            popUpTo(0) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    if (startDestination != null) {
        NavHost(
            navController = navController,
            startDestination = startDestination!!
        ) {

            // LOGIN
            composable(
                route = Screen.Login.route
            ) {
                LoginScreen(
                    viewModel = authViewModel,
                    onLoginSuccess = {
                        val currentUser = userViewModel.user.value
                        val destination = when (currentUser?.currentRole) {
                            "CUSTOMER" -> Screen.CustomerDashBoardScreen.route
                            "SERVICE_PROVIDER" -> {
                                if (currentUser.services.isEmpty()) {
                                    Screen.Services.route
                                } else {
                                    Screen.Dashboard.route
                                }
                            }
                            else -> Screen.Dashboard.route
                        }
                        navigateMain(destination)
                    },
                    onSignUpClick = {
                        navController.navigate(
                            Screen.Register.route
                        )
                    }
                )
            }

            // REGISTER
            composable(
                route = Screen.Register.route
            ) {
                RegisterScreen(
                    viewModel = authViewModel,
                    onLoginClick = {
                        navController.popBackStack()
                    },
                    onRegisterSuccess = { role ->
                        val destination = if (role == "SERVICE_PROVIDER") {
                            Screen.Services.route
                        } else {
                            Screen.CustomerDashBoardScreen.route
                        }
                        navigateMain(destination)
                    }
                )
            }

            // PROVIDER DASHBOARD
            composable(
                route = Screen.Dashboard.route
            ) {
                DashboardScreen(
                    viewModel = userViewModel,
                    jobViewModel = jobViewModel,
                    onHomeClick = {
                        navigateToTab(Screen.Dashboard.route)
                    },
                    onJobsClick = {
                        navigateToTab(Screen.Jobs.route)
                    },
                    onProfileClick = {
                        navigateToTab(Screen.Profile.route)
                    },
                    onMessageClick = {
                        navigateToTab(Screen.Conversations.route)
                    }
                )
            }

            // PROVIDER JOBS
            composable(
                route = Screen.Jobs.route
            ) {
                JobsScreen(
                    viewModel = jobViewModel,
                    onHomeClick = {
                        navigateToTab(Screen.Dashboard.route)
                    },
                    onProfileClick = {
                        navigateToTab(Screen.Profile.route)
                    },
                    onJobClick = {
                        // Job detail click if applicable
                    },
                    onChatClick = { job ->
                        if (
                            job.status == "Accepted" &&
                            job.customerId.isNotBlank()
                        ) {
                            chatViewModel.getOrCreateConversation(
                                otherUserId = job.customerId,
                                otherUserName = job.customerName.ifBlank { "Customer" },
                                jobId = job.id
                            ) { conversationId ->
                                navController.navigate(
                                    Screen.Chat.createRoute(
                                        conversationId = conversationId,
                                        otherUserId = job.customerId,
                                        otherUserName = job.customerName.ifBlank { "Customer" }
                                    )
                                )
                            }
                        }
                    }
                )
            }

            // CUSTOMER DASHBOARD
            composable(
                route = Screen.CustomerDashBoardScreen.route
            ) {
                CustomerDashboardScreen(
                    jobViewModel = jobViewModel,
                    onServiceClick = { service ->
                        navController.navigate(
                            Screen.PostJobs.createRoute(service)
                        )
                    },
                    onInboxClick = {
                        navigateToTab(Screen.Conversations.route)
                    },
                    userName = userViewModel.user.value?.name ?: "there",
                    onRequestsClick = {
                        navigateToTab(Screen.CustomerRequests.route)
                    },
                    onProfileClick = {
                        navigateToTab(Screen.Profile.route)
                    }
                )
            }

            // CUSTOMER REQUESTS
            composable(
                route = Screen.CustomerRequests.route
            ) {
                CustomerRequestsScreen(
                    viewModel = jobViewModel,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onInboxClick = {
                        navigateToTab(Screen.Conversations.route)
                    },

                    onChatClick = { job ->
                        if (
                            job.customerApprovedProvider &&
                            job.status == "Accepted" &&
                            job.providerId.isNotBlank()
                        ) {
                            chatViewModel.getOrCreateConversation(
                                otherUserId = job.providerId,
                                otherUserName = job.providerName,
                                jobId = job.id
                            ) { conversationId ->
                                navController.navigate(
                                    Screen.Chat.createRoute(
                                        conversationId = conversationId,
                                        otherUserId = job.providerId,
                                        otherUserName = job.providerName
                                    )
                                )
                            }
                        }
                    }
                )
            }

            // PROFILE
            composable(
                route = Screen.Profile.route
            ) {
                ProfileScreen(
                    viewModel = userViewModel,
                    onProfileClick = {
                        navigateToTab(Screen.Profile.route)
                    },
                    onJobsClick = {
                        navigateToTab(Screen.Jobs.route)
                    },
                    onHomeClick = {
                        val currentRole = userViewModel.user.value?.currentRole
                        if (currentRole == "CUSTOMER") {
                            navigateToTab(Screen.CustomerDashBoardScreen.route)
                        } else {
                            navigateToTab(Screen.Dashboard.route)
                        }
                    },
                    onSwitchAccountClick = { role ->
                        val destination = when (role) {
                            "CUSTOMER" -> Screen.CustomerDashBoardScreen.route

                            "SERVICE_PROVIDER" -> {
                                if (userViewModel.user.value?.services.isNullOrEmpty()) {
                                    Screen.Services.route
                                } else {
                                    Screen.Dashboard.route
                                }
                            }

                            else -> Screen.Login.route
                        }

                        navigateMain(destination)
                    }
                )
            }

            // SERVICES
            composable(
                route = Screen.Services.route
            ) {
                val servicesUserViewModel: UserViewModel = viewModel(
                    factory = UserViewModelFactory(
                        UserRepository()
                    )
                )

                ServicesScreen(
                    onSaveClick = { services ->
                        servicesUserViewModel.updateServices(
                            services
                        )

                        navigateMain(Screen.Dashboard.route)
                    }
                )
            }

            // POST JOB
            composable(
                route = Screen.PostJobs.route,
                arguments = listOf(
                    navArgument("service") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val context = LocalContext.current

                val service =
                    backStackEntry.arguments?.getString("service") ?: ""

                PostJobScreen(
                    initialService = service,
                    providerViewModel = providerViewModel,
                    userViewModel = userViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onPostJobClick = {
                            service,
                            description,
                            budget,
                            providerId,
                            providerName ->

                        val currentUser = userViewModel.user.value

                        if (currentUser != null) {
                            val job = Job(
                                customerId = currentUser.uid,
                                customerName = currentUser.name,
                                providerName = providerName,
                                providerId = providerId,
                                title = service,
                                description = description,
                                price = budget
                            )

                            jobViewModel.addJob(job) {
                                Toast.makeText(
                                    context,
                                    "Job posted successfully!",
                                    Toast.LENGTH_SHORT
                                ).show()

                                navController.popBackStack()
                            }
                        }
                    }
                )
            }

            // CONVERSATIONS / INBOX
            composable(
                route = Screen.Conversations.route
            ) {
                ConversationsScreen(
                    viewModel = chatViewModel,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onConversationClick = {
                            conversation,
                            otherUserId,
                            otherUserName ->

                        navController.navigate(
                            Screen.Chat.createRoute(
                                conversationId = conversation.id,
                                otherUserId = otherUserId,
                                otherUserName = otherUserName
                            )
                        )
                    }
                )
            }

            // INDIVIDUAL CHAT
            composable(
                route = Screen.Chat.route,
                arguments = listOf(
                    navArgument("conversationId") {
                        type = NavType.StringType
                    },
                    navArgument("otherUserId") {
                        type = NavType.StringType
                    },
                    navArgument("otherUserName") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->

                val conversationId =
                    backStackEntry.arguments
                        ?.getString("conversationId") ?: ""

                val otherUserId =
                    backStackEntry.arguments
                        ?.getString("otherUserId") ?: ""

                val otherUserName = Uri.decode(
                    backStackEntry.arguments
                        ?.getString("otherUserName") ?: "User"
                )

                ChatScreen(
                    viewModel = chatViewModel,
                    conversationId = conversationId,
                    otherUserId = otherUserId,
                    otherUserName = otherUserName,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
