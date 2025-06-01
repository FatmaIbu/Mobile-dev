package com.example.mobile_dev

import com.example.mobile_dev.LoginScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobile_dev.ui.theme.MobiledevTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobiledevTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(navController = navController, startDestination = "onboarding") {
                        composable("onboarding") {
                            OnboardingScreen(onStartClick = {
                                navController.navigate("login")
                            })
                        }
                        composable("login") {
                            LoginScreen(
                                onSignUpClick = { navController.navigate("signup") },
                                onLoginSuccess = { navController.navigate("home") })
                        }
                        composable("signup") {
                            SignUpScreen(
                                onSignUpClick = { navController.navigate("home") },
                                onLoginClick = { navController.navigate("login") }
                            )
                        }
                        composable("home") {
                            HomeScaffold(
                                currentRoute = "home",
                                onNavItemSelected = { route ->
                                    when (route) {
                                        "Home" -> {} // Already on home
                                        "Calendar" -> navController.navigate("calendar")
                                        "Profile" -> navController.navigate("profile")
                                    }
                                },
                                onAddTaskClick = { navController.navigate("addtask") }

                            )
                        }
                        composable("tasks") {
                            TasksScreen()
                        }
                        composable("addtask") {
                            AddTaskScreen(
                                onSaveClick = { task ->
                                    // You can add logic to save task to a shared ViewModel later
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable("profile") {
                            ProfileScreen(
                                onLogoutClick = {
                                    navController.navigate("login") {
                                        popUpTo("home") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("calendar") {
                            CalendarScreen()
                        }

                        /*
                                                composable("calendar") { CalendarScreen() }
                                                composable("profile") { ProfileScreen() }*/
                    }
                }
            }
        }
    }
}


/*@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    MobiledevTheme {
        OnboardingScreen(onStartClick = {})
    }
}
*/
@Preview(showBackground = true)
@Composable
fun OnboardingPreview() {
    MobiledevTheme {
        HomeScreen()
    }
}