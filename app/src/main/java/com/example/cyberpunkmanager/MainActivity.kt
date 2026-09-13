package com.example.cyberpunkmanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.cyberpunkmanager.ui.AdminButton
import com.example.cyberpunkmanager.ui.AdminScreen
import com.example.cyberpunkmanager.ui.CategoryScreen
import com.example.cyberpunkmanager.ui.DashboardScreen
import com.example.cyberpunkmanager.ui.DetailScreen
import com.example.cyberpunkmanager.ui.WelcomeScreen
import com.example.cyberpunkmanager.ui.theme.CyberpunkManagerTheme
import com.example.cyberpunkmanager.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CyberpunkManagerTheme {
                MainApp()
            }
        }
    }
}

@Composable
fun MainApp() {
    val navController = rememberNavController()
    val viewModel: AppViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(navController = navController, startDestination = "welcome") {
            composable("welcome") {
                WelcomeScreen { navController.navigate("dashboard") }
            }
            composable("dashboard") {
                viewModel.setSearchQuery("") // Reset search on back to dashboard
                DashboardScreen { category ->
                    viewModel.loadCategory(category)
                    navController.navigate("category/$category")
                }
            }
            composable("category/{category}") { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category") ?: ""
                CategoryScreen(
                    category = category,
                    viewModel = viewModel,
                    onItemClick = { navController.navigate("detail") },
                    onBack = { navController.popBackStack() }
                )
            }
            composable("detail") {
                DetailScreen(viewModel) {
                    navController.popBackStack()
                }
            }
            composable("admin") {
                AdminScreen(
                    viewModel = viewModel,
                    onDetailClick = { navController.navigate("detail") },
                    onBack = { navController.popBackStack() }
                )
            }
        }
        
        if (currentRoute != "admin") {
            AdminButton {
                navController.navigate("admin")
            }
        }
    }
}
