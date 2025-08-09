package eu.tutorials

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

import eu.tutorials.motiveoapp.Screen.HomeScreen
import eu.tutorials.motiveoapp.Screen.LoginScreen
import eu.tutorials.motiveoapp.Screen.SignUpScreen
import eu.tutorials.motiveoapp.pages.GoalsPage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(modifier: Modifier = Modifier) {

    val navController = rememberNavController()
    val isLoggedIn  = Firebase.auth.currentUser!=null
    val firstPage = if(isLoggedIn) "home" else "giris_ekrani"
    NavHost(navController=navController, startDestination = firstPage)
    {
        composable(route = "giris_ekrani")
        {
            LoginScreen(navController = navController)
        }

        composable(route = "kayit_ol")
        {
            SignUpScreen(navController = navController)
        }

        composable(route = "home")
        {
            HomeScreen(navController = navController)
        }

    }

}