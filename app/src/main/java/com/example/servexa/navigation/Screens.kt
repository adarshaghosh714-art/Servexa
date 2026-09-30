package com.example.servexa.navigation



sealed class Screen(val route: String) {
    data object Splash : Screen("splash")

    data object Login : Screen("login")

    data object Register : Screen("register")

    data object Dashboard : Screen("dashboard")

    data object Jobs : Screen("jobs")

    data object CustomerDashBoardScreen : Screen("customers")

    data object Profile : Screen("profile")
    data object Services : Screen("services")
    data object PostJobs : Screen("post_job/{service}") {
        fun createRoute(service: String): String {
            return "post_job/$service"
        }
    }
    data object CustomerRequests : Screen("customer_requests")
}