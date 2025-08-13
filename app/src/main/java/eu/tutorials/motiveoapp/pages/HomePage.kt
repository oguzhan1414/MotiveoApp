package eu.tutorials.motiveoapp.pages

import GoalsViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.app.viewmodel.ChronometerViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import eu.tutorials.motiveoapp.utils.MotivationProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    viewModel: ChronometerViewModel = viewModel(),
    goalsViewModel: GoalsViewModel = viewModel()
) {
    var name by remember { mutableStateOf("") }

    // Kullanıcı adı Firestore'dan çekiliyor
    LaunchedEffect(Unit) {
        Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
            .get().addOnCompleteListener {
                name = it.result?.get("name").toString()
            }
    }

    val goals by goalsViewModel.todayGoals.collectAsState()
    val motivationMessage by remember { mutableStateOf(MotivationProvider.getTodayMotivation()) }

    // ******** XP Değeri Burada ViewModel'den alınacak ********
    // XP listener'ı başlatmak için LaunchedEffect
    LaunchedEffect(Unit) {
        viewModel.startXpListener() // ViewModel'deki XP dinleyicisi başlatılır
    }

    val currentXp = viewModel.xp  // XP mutableState değişkenini oku

    // Renk paleti
    val primaryColor = Color(0xFF6C63FF)
    val secondaryColor = Color(0xFF4D8AF0)
    val gradientBackground = Brush.verticalGradient(
        colors = listOf(Color(0xFFF8F9FF), Color(0xFFE9F0FF))
    )
    val cardBackground = Color(0xFFFFFFFF)
    val textPrimary = Color(0xFF2D3748)
    val textSecondary = Color(0xFF718096)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradientBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Welcome back,",
                        fontSize = 18.sp,
                        color = textSecondary
                    )
                    Text(
                        text = name,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = textPrimary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // ******** XP Değeri Dinamik Gösteriliyor ********
                Surface(
                    color = primaryColor.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = "Star",
                            tint = primaryColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "$currentXp XP", // Burada sabit "128 XP" yerine güncel XP gösteriliyor
                            color = primaryColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Timer Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 12.dp,
                        shape = RoundedCornerShape(24.dp),
                    ),
                shape = RoundedCornerShape(24.dp),
                color = cardBackground
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "⏱ Focus Timer",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = textPrimary
                    )

                    Text(
                        text = formatTime(viewModel.elapsedTime),
                        fontSize = 48.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = primaryColor,
                        letterSpacing = 1.sp
                    )

                    when {
                        !viewModel.isRunning && !viewModel.isPaused -> {
                            Button(
                                onClick = { viewModel.startTimer() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
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
                                Text("Start Session", fontSize = 16.sp)
                            }
                        }
                        viewModel.isRunning -> {
                            Button(
                                onClick = { viewModel.pauseTimer() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFF7043),
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Take a Break", fontSize = 16.sp)
                            }
                        }
                        viewModel.isPaused -> {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Button(
                                    onClick = { viewModel.continueTimer() },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = primaryColor,
                                        contentColor = Color.White
                                    )
                                ) {
                                    Text("Resume", fontSize = 16.sp)
                                }

                                Button(
                                    onClick = { viewModel.endTimer() },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFF5F5F5),
                                        contentColor = textPrimary
                                    ),
                                    border = ButtonDefaults.outlinedButtonBorder
                                ) {
                                    Text("Finish", fontSize = 16.sp)
                                }
                            }
                        }
                    }
                }
            }

            // Goals Progress
            val completedCount = goals.count { it.isCompleted }
            val totalCount = goals.size
            val progress = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp)
                    ),
                shape = RoundedCornerShape(24.dp),
                color = cardBackground
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "🎯 Today's Goals",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp,
                            color = textPrimary
                        )
                        Text(
                            "$completedCount/$totalCount",
                            color = if (completedCount == totalCount) Color(0xFF4CAF50) else textSecondary,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    LinearProgressIndicator(
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = primaryColor,
                        trackColor = Color(0xFFEDF2F7)
                    )

                    if (totalCount > 0) {
                        Text(
                            when {
                                progress == 1f -> "Amazing! You completed all goals! 🎉"
                                progress > 0.7f -> "Almost there! Keep going! 💪"
                                progress > 0.3f -> "Good progress! You got this! ✨"
                                else -> "Every small step counts! 👣"
                            },
                            color = textSecondary,
                            fontSize = 14.sp
                        )
                    } else {
                        Text(
                            "No goals set for today. Add some to get started!",
                            color = textSecondary,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            // Motivation Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(24.dp)
                    ),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFFF0F7FF)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        /*
                        // Kendi ikonunu buraya ekle
                        Icon(
                            painter = painterResource(id = R.drawable.ic_quote),
                            contentDescription = "Quote",
                            tint = primaryColor,
                            modifier = Modifier.size(20.dp)
                        )
                        */
                        Text(
                            "Daily Inspiration",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp,
                            color = primaryColor
                        )
                    }

                    Text(
                        motivationMessage,
                        style = MaterialTheme.typography.bodyLarge,
                        color = textPrimary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        lineHeight = 24.sp
                    )

                    Text(
                        "— Motiveo App",
                        color = textSecondary,
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.End)
                    )
                }
            }
        }
    }
}

fun formatTime(seconds: Long): String {
    val hours = seconds / 3600
    val minutes = (seconds % 3600) / 60
    val secs = seconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, secs)
}
