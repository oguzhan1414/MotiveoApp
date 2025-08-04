package eu.tutorials.motiveoapp.model
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Person

data class NavigationItem(
    val title : String,
    val icon : ImageVector,

)

val navigationItems = listOf(
    NavigationItem(
        title = "Home",
        icon = Icons.Filled.Home

    ),
    NavigationItem(
        title = "Goals",
        icon = Icons.Filled.Flag

    ),
    NavigationItem(
        title = "Add",
        icon = Icons.Filled.AddCircle

    ),
    NavigationItem(
        title = "Progress",
        icon = Icons.Filled.BarChart

    ),
    NavigationItem(
        title = "Profile",
        icon = Icons.Filled.Person

    )
)