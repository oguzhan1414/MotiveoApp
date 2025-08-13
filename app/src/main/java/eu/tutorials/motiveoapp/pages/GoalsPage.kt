package eu.tutorials.motiveoapp.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.ListenerRegistration
import eu.tutorials.motiveoapp.model.GoalItem
import eu.tutorials.motiveoapp.viewmodel.addItemToGoals
import eu.tutorials.motiveoapp.viewmodel.listenTodaysGoals
import eu.tutorials.motiveoapp.viewmodel.toggleGoalCompletion

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoalsPage(modifier: Modifier = Modifier) {
    // Renk paleti
    val primaryColor = Color(0xFF6C63FF)
    val secondaryColor = Color(0xFF4D8AF0)
    val background = Color(0xFFF8F9FF)
    val cardBackground = Color(0xFFFFFFFF)
    val textPrimary = Color(0xFF2D3748)
    val textSecondary = Color(0xFF718096)

    var goals by remember { mutableStateOf<List<GoalItem>>(emptyList()) }
    var newGoalText by remember { mutableStateOf(TextFieldValue("")) }
    var listenerRegistration by remember { mutableStateOf<ListenerRegistration?>(null) }
    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        listenerRegistration = listenTodaysGoals { fetchedGoals ->
            goals = fetchedGoals
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            listenerRegistration?.remove()
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(background)
            .padding(24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Today's Goals",
                    style = MaterialTheme.typography.headlineSmall,
                    color = textPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${goals.count { it.isCompleted }}/${goals.size} completed",
                    style = MaterialTheme.typography.bodyMedium,
                    color = textSecondary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Icon(
                imageVector = Icons.Filled.Task,
                contentDescription = "Goals",
                tint = primaryColor,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Add Goal Input
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newGoalText,
                onValueChange = { newGoalText = it },
                placeholder = { Text("What's your goal today?", color = textSecondary) },
                modifier = Modifier
                    .weight(1f)
                    .shadow(4.dp, RoundedCornerShape(16.dp), clip = true),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    containerColor = cardBackground,
                    focusedBorderColor = primaryColor,
                    unfocusedBorderColor = Color.Transparent
                ),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.AddCircleOutline,
                        contentDescription = "Add",
                        tint = primaryColor
                    )
                }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Button(
                onClick = {
                    if (newGoalText.text.trim().isNotEmpty()) {
                        addItemToGoals(newGoalText.text.trim())
                        newGoalText = TextFieldValue("")
                        focusManager.clearFocus() // Klavyeyi kapatır
                    }
                },
                modifier = Modifier.height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = primaryColor,
                    contentColor = Color.White
                ),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 4.dp,
                    pressedElevation = 0.dp
                )
            ) {
                Text("Add", fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Goals List
        if (goals.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.Checklist,
                    contentDescription = "Empty",
                    tint = textSecondary.copy(alpha = 0.3f),
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = "No goals yet",
                    color = textSecondary,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Add your first goal to get started!",
                    color = textSecondary.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(goals) { goal ->
                    GoalCard(
                        goal = goal,
                        onToggle = { toggleGoalCompletion(it) },
                        primaryColor = primaryColor,
                        textPrimary = textPrimary,
                        textSecondary = textSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun GoalCard(
    goal: GoalItem,
    modifier: Modifier = Modifier,
    primaryColor: Color,
    textPrimary: Color,
    textSecondary: Color,
    onToggle: (GoalItem) -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (goal.isCompleted) primaryColor.copy(alpha = 0.08f) else Color.White
    )
    val iconColor by animateColorAsState(
        targetValue = if (goal.isCompleted) primaryColor else textSecondary
    )
    val textColor by animateColorAsState(
        targetValue = if (goal.isCompleted) textPrimary.copy(alpha = 0.7f) else textPrimary
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .clickable { onToggle(goal) },
        color = bgColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (goal.isCompleted) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = goal.title,
                    fontSize = 16.sp,
                    color = textColor,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = if (goal.isCompleted) FontWeight.Medium else FontWeight.SemiBold,
                        textDecoration = if (goal.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                    )
                )
            }

            if (goal.isCompleted) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Completed",
                    tint = primaryColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
