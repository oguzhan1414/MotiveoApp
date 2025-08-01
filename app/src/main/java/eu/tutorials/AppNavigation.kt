package eu.tutorials

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.tutorials.motiveoapp.Screen.AuthScreen
import eu.tutorials.motiveoapp.Screen.SignUpScreen

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    NavHost(navController=navController, startDestination = "giris_ekrani")
    {
        composable(route = "giris_ekrani")
        {
            AuthScreen(navController = navController)
        }

        composable(route = "kayit_ol")
        {
            SignUpScreen(navController = navController)
        }
    }

}