import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.tutorials.motiveoapp.model.GoalItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.google.firebase.firestore.ListenerRegistration
import java.text.SimpleDateFormat
import java.util.*

class GoalsViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    private val _todayGoals = MutableStateFlow<List<GoalItem>>(emptyList())
    val todayGoals: StateFlow<List<GoalItem>> = _todayGoals

    private var listenerRegistration: ListenerRegistration? = null

    init {
        if (userId != null) {
            startListeningTodayGoals()
        }
    }

    private fun startListeningTodayGoals() {
        val userRef = db.collection("users").document(userId!!)
        listenerRegistration = userRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                println("Goal listen error: ${error.message}")
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                val goalsByDate = snapshot.get("goalsByDate") as? Map<String, List<Map<String, Any>>> ?: emptyMap()
                val todayGoalsRaw = goalsByDate[today] ?: emptyList()
                val goals = todayGoalsRaw.map {
                    GoalItem(
                        title = it["title"] as? String ?: "",
                        timestamp = (it["timestamp"] as? Long) ?: System.currentTimeMillis(),
                        isCompleted = it["isCompleted"] as? Boolean ?: false
                    )
                }
                _todayGoals.value = goals
            } else {
                _todayGoals.value = emptyList()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        listenerRegistration?.remove()
    }
}
