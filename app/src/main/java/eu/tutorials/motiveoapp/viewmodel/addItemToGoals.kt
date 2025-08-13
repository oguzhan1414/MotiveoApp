package eu.tutorials.motiveoapp.viewmodel

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import eu.tutorials.motiveoapp.model.GoalItem
import java.text.SimpleDateFormat
import java.util.*


fun toggleGoalCompletion(goal: GoalItem) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)

    userRef.get().addOnSuccessListener { snapshot ->
        val goalsByDate = snapshot.get("goalsByDate") as? Map<String, List<Map<String, Any>>> ?: emptyMap()
        val todaysGoalsRaw = goalsByDate[today] ?: emptyList()

        // Güncel goals listesini GoalItem olarak oluştur
        val todaysGoals = todaysGoalsRaw.map {
            GoalItem(
                title = it["title"] as? String ?: "",
                timestamp = (it["timestamp"] as? Long) ?: System.currentTimeMillis(),
                isCompleted = (it["isCompleted"] as? Boolean) ?: false
            )
        }.toMutableList()

        // Toggle işlemi
        val index = todaysGoals.indexOfFirst { it.title == goal.title && it.timestamp == goal.timestamp }
        if (index != -1) {
            val current = todaysGoals[index]
            todaysGoals[index] = current.copy(isCompleted = !current.isCompleted)
        }

        // Firestore'a tekrar yaz
        val updatedGoalsByDate = goalsByDate.toMutableMap()
        updatedGoalsByDate[today] = todaysGoals.map {
            mapOf(
                "title" to it.title,
                "timestamp" to it.timestamp,
                "isCompleted" to it.isCompleted
            )
        }

        userRef.update("goalsByDate", updatedGoalsByDate)
    }
}

fun addItemToGoals(newGoal: String) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    val goalItem = GoalItem(title = newGoal, isCompleted = false)

    val userRef = FirebaseFirestore.getInstance().collection("users").document(userId)

    FirebaseFirestore.getInstance().runTransaction { transaction ->
        val snapshot = transaction.get(userRef)

        val currentGoalsByDate = snapshot.get("goalsByDate") as? Map<String, List<Map<String, Any>>> ?: emptyMap()

        val todaysGoalsRaw = currentGoalsByDate[today] ?: emptyList()

        val todaysGoals = todaysGoalsRaw.map {
            GoalItem(
                title = it["title"] as? String ?: "",
                timestamp = (it["timestamp"] as? Long) ?: System.currentTimeMillis(),
                isCompleted = it["isCompleted"] as? Boolean ?: false  // Burayı da unutma!
            )
        }.toMutableList()

        todaysGoals.add(goalItem)

        val updatedGoalsByDate = currentGoalsByDate.toMutableMap()
        updatedGoalsByDate[today] = todaysGoals.map {
            mapOf(
                "title" to it.title,
                "timestamp" to it.timestamp,
                "isCompleted" to it.isCompleted  // Burayı ekle
            )
        }

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
                    isCompleted = it["isCompleted"] as? Boolean ?: false  // burayı düzelt
                )
            }
            onDataChange(todayGoals)
        } else {
            onDataChange(emptyList())
        }
    }
}

