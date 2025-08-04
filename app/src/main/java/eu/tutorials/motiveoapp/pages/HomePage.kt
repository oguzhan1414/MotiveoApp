package eu.tutorials.motiveoapp.pages


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFc2e9fb), Color(0xFF81a4fd))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                text = "Welcome back 👋",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1a1a1a)
            )

            Text(
                text = "Here's your progress for today:",
                fontSize = 16.sp,
                color = Color(0xFF333333)
            )

            // Günlük hedef kartı
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("🎯 Daily Goals", fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("• 2 of 5 completed", color = Color.Gray)
                }
            }

            // Motivasyon mesajı kartı
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFe3f2fd)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("✨ Today's Motivation", fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("\"Success doesn’t come from what you do occasionally, it comes from what you do consistently.\"",
                        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                        color = Color(0xFF333333)
                    )
                }
            }

            // Daha sonra burada grafik, hızlı aksiyonlar veya öneri kartları da eklenebilir.
        }
    }
}