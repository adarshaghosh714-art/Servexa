package com.example.servexa.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

import com.example.servexa.model.Job

import com.example.servexa.repository.AuthRepository
import com.example.servexa.repository.JobRepository
import com.example.servexa.repository.ProviderRepository
import com.example.servexa.repository.UserRepository

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

    var startDestination by remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        userViewModel.checkLoginStatus { user ->

            startDestination = when (user?.currentRole) {
                "CUSTOMER" -> Screen.CustomerDashBoardScreen.route
                "SERVICE_PROVIDER" -> Screen.Dashboard.route
                else -> Screen.Login.route
            }
        }
    }


    // NAVIGATION GRAPH

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
                        navController.navigate(
                            Screen.Dashboard.route
                        )
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

                        if (role == "SERVICE_PROVIDER") {

                            navController.navigate(
                                Screen.Services.route
                            )

                        } else {

                            navController.navigate(
                                Screen.CustomerDashBoardScreen.route
                            )
                        }
                    }
                )
            }


            // PROVIDER DASHBOARD


            composable(
                route = Screen.Dashboard.route
            ) {

                DashboardScreen(
                    viewModel = userViewModel,

                    onHomeClick = {
                        navController.navigate(
                            Screen.Dashboard.route
                        )
                    },

                    onJobsClick = {
                        navController.navigate(
                            Screen.Jobs.route
                        )
                    },

                    onProfileClick = {
                        navController.navigate(
                            Screen.Profile.route
                        )
                    }
                )
            }


            // PROVIDER JOBS


            composable(
                route = Screen.Jobs.route
            ) {

                val jobViewModel: JobViewModel = viewModel(
                    factory = JobViewModelFactory(
                        JobRepository()
                    )
                )

                JobsScreen(
                    viewModel = jobViewModel,

                    onHomeClick = {
                        navController.navigate(
                            Screen.Dashboard.route
                        )
                    },

                    onProfileClick = {
                        navController.navigate(
                            Screen.Profile.route
                        )
                    },

                    onJobClick = {
                        navController.navigate(
                            Screen.Jobs.route
                        )
                    }
                )
            }


            // CUSTOMER DASHBOARD


            composable(
                route = Screen.CustomerDashBoardScreen.route
            ) {
                val jobViewModel: JobViewModel = viewModel(
                    factory = JobViewModelFactory(
                        JobRepository()
                    )
                )
                CustomerDashboardScreen(
                    jobViewModel = jobViewModel,

                    onServiceClick = { service ->
                        navController.navigate(
                            Screen.PostJobs.createRoute(service)
                        )
                    },
                    userName = userViewModel.user.value?.name ?: "there",


                    onRequestsClick = {

                        navController.navigate(
                            Screen.CustomerRequests.route
                        )
                    },

                    onProfileClick = {

                        // We will connect this later
                    }
                )
            }


            // CUSTOMER REQUESTS


            composable(
                route = Screen.CustomerRequests.route
            ) {

                val jobViewModel: JobViewModel = viewModel(
                    factory = JobViewModelFactory(
                        JobRepository()
                    )
                )

                CustomerRequestsScreen(
                    viewModel = jobViewModel,

                    onBackClick = {
                        navController.popBackStack()
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
                        navController.navigate(
                            Screen.Profile.route
                        )
                    },

                    onJobsClick = {
                        navController.navigate(
                            Screen.Jobs.route
                        )
                    },

                    onHomeClick = {
                        navController.navigate(
                            Screen.Dashboard.route
                        )
                    }
                )
            }


            // SERVICES


            composable(
                route = Screen.Services.route
            ) {

                val userViewModel: UserViewModel = viewModel(
                    factory = UserViewModelFactory(
                        UserRepository()
                    )
                )

                ServicesScreen(

                    onSaveClick = { services ->

                        userViewModel.updateServices(
                            services
                        )

                        navController.navigate(
                            Screen.Dashboard.route
                        )
                    }
                )
            }


            // POST JOB


            composable(
                arguments = listOf(
                    navArgument("service") {
                        type = NavType.StringType
                    }
                ),
                route = Screen.PostJobs.route
            ) { backStackEntry ->
                val service = backStackEntry.arguments?.getString("service") ?: ""

                val jobViewModel: JobViewModel = viewModel(
                    factory = JobViewModelFactory(
                        JobRepository()
                    )
                )

                PostJobScreen(
                    initialService = service,

                    providerViewModel = providerViewModel,

                    userViewModel = userViewModel,

                    onBackClick = {
                        navController.popBackStack()
                    },

                    onPostJobClick = { service,
                                       description,
                                       budget,
                                       providerId,
                        providerName->

                        val currentUser =
                            userViewModel.user.value

                        if (currentUser != null) {

                            val job = Job(

                                customerId =
                                    currentUser.uid,

                                customerName =
                                    currentUser.name,

                                providerName = providerName,

                                title =
                                    service,

                                description =
                                    description,

                                price =
                                    budget,

                            )

                            jobViewModel.addJob(job)
                        }
                    }
                )
            }
        }
    }
}