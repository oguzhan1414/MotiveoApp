package eu.tutorials.motiveoapp.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProgressPage(modifier: Modifier = Modifier) {
    // Örnek değerler (ileride Firestore'dan alınacak)
    val totalGoals = 5
    val completedGoals = 2
    val progress = completedGoals.toFloat() / totalGoals

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF1F8E9))
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        Text("📊 Your Progress", fontSize = 24.sp, color = Color(0xFF2E7D32))

        // İlerleme çubuğu
        Text("Completion Rate", fontSize = 16.sp)
        LinearProgressIndicator(
            progress = progress,
            modifier = Modifier
                .fillMaxWidth()
                .height(10.dp)
                .clip(RoundedCornerShape(12.dp)),
            color = Color(0xFF66BB6A)
        )

        // Sayısal özet kartları
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            StatCard(title = "Total", value = "$totalGoals")
            StatCard(title = "Done", value = "$completedGoals")
            StatCard(title = "Left", value = "${totalGoals - completedGoals}")
        }
    }
}

@Composable
fun StatCard(title: String, value: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .width(100.dp)
            .height(80.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(title, fontSize = 14.sp, color = Color.Gray)
            Text(value, fontSize = 20.sp, color = Color.Black)
        }
    }
}