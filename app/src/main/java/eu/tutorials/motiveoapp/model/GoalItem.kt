package eu.tutorials.motiveoapp.model

data class GoalItem(
    val title: String = "",
    val timestamp: Long = System.currentTimeMillis(), // Kayıt zamanı (milisaniye)
    val isCompleted: Boolean
)

data class GoalsModel(
    val name: String = "",
    val email: String = "",
    val uid: String = "",
    val goalsByDate: Map<String, List<GoalItem>> = emptyMap() // Tarih -> Görev listesi
)