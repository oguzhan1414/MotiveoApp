package eu.tutorials.motiveoapp.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GoalsPage(modifier: Modifier = Modifier) {
    val dummyGoals = listOf(
        GoalItem("Run 5km", true),
        GoalItem("Read 30 pages", false),
        GoalItem("Meditate 10 mins", false),
        GoalItem("Learn 20 English words", true)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF0F4FF))
            .padding(16.dp)
    ) {
        Text(
            text = "🎯 My Goals",
            fontSize = 24.sp,
            color = Color(0xFF1A237E),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(dummyGoals) { goal ->
                GoalCard(goal)
            }
        }
    }
}

data class GoalItem(val title: String, val isCompleted: Boolean)

@Composable
fun GoalCard(goal: GoalItem) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (goal.isCompleted) Color(0xFFE0F7FA) else Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(goal.title, fontSize = 16.sp)
            Icon(
                imageVector = if (goal.isCompleted) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = null,
                tint = if (goal.isCompleted) Color(0xFF00BCD4) else Color.Gray
            )
        }
    }
}