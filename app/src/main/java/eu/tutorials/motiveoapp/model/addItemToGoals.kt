package eu.tutorials.motiveoapp.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.text.SimpleDateFormat
import java.util.*

fun addItemToGoals(newGoal: String) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val goalItem = GoalItem(title = newGoal, isCompleted = false)

    val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)

    FirebaseFirestore.getInstance().runTransaction { transaction ->
        val snapshot = transaction.get(userRef)

        val currentGoalsByDate = snapshot.get("goalsByDate") as? Map<String, List<Map<String, Any>>> ?: emptyMap()

        val todaysGoalsRaw = currentGoalsByDate[today] ?: emptyList()

        // Map’leri GoalItem’a dönüştür
        val todaysGoals = todaysGoalsRaw.map {
            GoalItem(
                title = it["title"] as? String ?: "",
                timestamp = (it["timestamp"] as? Long) ?: System.currentTimeMillis(),
                isCompleted = false
            )
        }.toMutableList()

        // Yeni hedefi ekle
        todaysGoals.add(goalItem)

        // Güncellenmiş map
        val updatedGoalsByDate = currentGoalsByDate.toMutableMap()
        updatedGoalsByDate[today] = todaysGoals.map { mapOf("title" to it.title, "timestamp" to it.timestamp) }

        transaction.update(userRef, "goalsByDate", updatedGoalsByDate)
    }
}

fun listenTodaysGoals(onDataChange: (List<GoalItem>) -> Unit): ListenerRegistration? {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return null
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)

    return userRef.addSnapshotListener { snapshot, error ->
        if (error != null) {
            println("Veri dinlerken hata: ${error.message}")
            return@addSnapshotListener
        }
        if (snapshot != null && snapshot.exists()) {
            val goalsByDate = snapshot.get("goalsByDate") as? Map<String, List<Map<String, Any>>> ?: emptyMap()
            val todayGoalsRaw = goalsByDate[today] ?: emptyList()
            val todayGoals = todayGoalsRaw.map {
                GoalItem(
                    title = it["title"] as? String ?: "",
                    timestamp = (it["timestamp"] as? Long) ?: System.currentTimeMillis(),
                    isCompleted = false
                )
            }
            onDataChange(todayGoals)
        } else {
            onDataChange(emptyList())
        }
    }
}

