package eu.tutorials.motiveoapp.pages

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.YearMonth






@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProgressPage(
    modifier: Modifier = Modifier,
    weeklyTaskCompletion: List<Int> = List(7) { (0..5).random() } // Örnek dışarıdan gelen veri
) {
    val currentYearMonth = YearMonth.now()
    val daysInMonth = currentYearMonth.lengthOfMonth()
    val firstDayOfMonth = currentYearMonth.atDay(1)
    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7

    val calendarDays = mutableListOf<LocalDate?>()
    repeat(firstDayOfWeek) { calendarDays.add(null) }
    for (day in 1..daysInMonth) {
        calendarDays.add(LocalDate.of(currentYearMonth.year, currentYearMonth.month, day))
    }

    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    val emojiMap = remember { mutableStateMapOf<LocalDate, String>() }
    val emojis = listOf("😄", "😐", "😔", "😡", "😍")

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "📅 Monthly Calendar",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(7) { index ->
                val dayName = DayOfWeekShortName(index)
                Text(
                    text = dayName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            items(calendarDays.size) { index ->
                val date = calendarDays[index]
                CalendarDayCell(
                    date = date,
                    isSelected = date == selectedDate,
                    emoji = emojiMap[date],
                    onClick = { clickedDate ->
                        if (clickedDate != null) {
                            selectedDate = clickedDate
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("How was your day? Select an emoji:", fontWeight = FontWeight.SemiBold)
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            emojis.forEach { emoji ->
                val selectedEmoji = emojiMap[selectedDate]
                Text(
                    text = emoji,
                    fontSize = 32.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(if (selectedEmoji == emoji) Color(0xFFBBDEFB) else Color.Transparent)
                        .padding(8.dp)
                        .clickable {
                            emojiMap[selectedDate] = emoji
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "📊 Weekly Task Completion",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        BarChartWeekly(weeklyTaskCompletion)
    }
}

@Composable
fun BarChartWeekly(completions: List<Int>) {
    val maxCompleted = (completions.maxOrNull() ?: 1).coerceAtLeast(1)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        completions.forEachIndexed { index, completedTasks ->
            val barHeight = (completedTasks.toFloat() / maxCompleted) * 150
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .height(barHeight.dp)
                        .width(20.dp)
                        .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                        .background(Color(0xFF1976D2))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "D${index + 1}",
                    fontSize = 12.sp
                )
                Text(
                    text = completedTasks.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarDayCell(date: LocalDate?, isSelected: Boolean, emoji: String?, onClick: (LocalDate?) -> Unit) {
    val bgColor = if (isSelected) Color(0xFFBBDEFB) else Color.Transparent
    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable { onClick(date) }
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFF1976D2) else Color(0xFFB0BEC5),
                shape = RoundedCornerShape(12.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = date?.dayOfMonth?.toString() ?: "",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color(0xFF37474F)
            )
            if (emoji != null) {
                Text(text = emoji, fontSize = 20.sp)
            }
        }
    }
}

fun DayOfWeekShortName(index: Int): String {
    val days = listOf("P", "S", "Ç", "P", "C", "Ct", "Pz")
    return days.getOrElse(index) { "" }
}