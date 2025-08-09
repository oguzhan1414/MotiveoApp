package eu.tutorials.motiveoapp.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.ListenerRegistration
import eu.tutorials.motiveoapp.model.GoalItem
import eu.tutorials.motiveoapp.model.addItemToGoals
import eu.tutorials.motiveoapp.model.listenTodaysGoals


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsPage(modifier: Modifier = Modifier) {
    var goals by remember { mutableStateOf<List<GoalItem>>(emptyList()) }
    var newGoalText by remember { mutableStateOf(TextFieldValue("")) }
    var listenerRegistration by remember { mutableStateOf<ListenerRegistration?>(null) }

    // Firestore'dan gerçek zamanlı dinleme
    LaunchedEffect(Unit) {
        listenerRegistration = listenTodaysGoals { fetchedGoals ->
            goals = fetchedGoals
        }
    }

    // Dinleyici temizleme
    DisposableEffect(Unit) {
        onDispose {
            listenerRegistration?.remove()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F9FF))
            .padding(16.dp)
    ) {
        Text(
            text = "🎯 My Daily Goals",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF0D47A1),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = newGoalText,
                onValueChange = { newGoalText = it },
                placeholder = { Text("Add new goal...") },
                modifier = Modifier
                    .weight(1f)
                    .shadow(4.dp, RoundedCornerShape(12.dp)),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = {
                    val trimmedText = newGoalText.text.trim()
                    if (trimmedText.isNotEmpty()) {
                        addItemToGoals(trimmedText)
                        newGoalText = TextFieldValue("")
                    }
                },
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Add")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(goals) { goal ->
                GoalCard(
                    goal = GoalItem(
                        title = goal.title,
                        isCompleted = false
                    ),
                    onToggle = {
                        // İstersen buraya completion toggle için firestore update ekleyebilirsin
                    }
                )
            }
        }
    }
}


@Composable
fun GoalCard(goal: GoalItem, modifier: Modifier = Modifier, onToggle: (GoalItem) -> Unit) {
    val bgColor by animateColorAsState(
        targetValue = if (goal.isCompleted) Color(0xFFD0F0ED) else Color.White
    )
    val iconTint by animateColorAsState(
        targetValue = if (goal.isCompleted) Color(0xFF00796B) else Color.Gray
    )
    val textColor by animateColorAsState(
        targetValue = if (goal.isCompleted) Color(0xFF004D40) else Color(0xFF212121)
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .fillMaxWidth()
            .shadow(6.dp, RoundedCornerShape(16.dp))
            .background(bgColor)
            .clickable { onToggle(goal) },
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 24.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = goal.title,
                fontSize = 18.sp,
                color = textColor,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = if (goal.isCompleted) FontWeight.Bold else FontWeight.Medium,
                    textDecoration = if (goal.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                )
            )
            Icon(
                imageVector = if (goal.isCompleted) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                contentDescription = if (goal.isCompleted) "Completed" else "Not completed",
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
