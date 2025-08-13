package eu.tutorials.motiveoapp.Screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import eu.tutorials.motiveoapp.model.navigationItems

import eu.tutorials.motiveoapp.pages.GoalsPage
import eu.tutorials.motiveoapp.pages.HomePage
import eu.tutorials.motiveoapp.pages.ProfilePage
import eu.tutorials.motiveoapp.pages.ProgressPage

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController) {
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White.copy(alpha = 0.95f)) {
                navigationItems.forEachIndexed { index, navigationItem ->
                    NavigationBarItem(
                        selected = index == selectedIndex,
                        onClick = { selectedIndex = index },
                        icon = {
                            Icon(
                                imageVector = navigationItem.icon,
                                contentDescription = navigationItem.title,
                                tint = if (index == selectedIndex) Color(0xFF2196F3) else Color.Gray
                            )
                        },
                        label = {
                            Text(
                                text = navigationItem.title,
                                color = if (index == selectedIndex) Color(0xFF2196F3) else Color.Gray
                            )
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        ContentScreen(
            modifier = modifier.padding(paddingValues),
            selectedIndex = selectedIndex,
            navController = navController   // burada navController geçiriyoruz
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    navController: NavController    // navController parametresi eklendi
) {
    when (selectedIndex) {
        0 -> HomePage(modifier)
        1 -> GoalsPage(modifier)
        2 -> ProgressPage(modifier)
        3 -> ProfilePage(modifier, navController)  // navController burada verildi
    }
}
