package eu.tutorials.motiveoapp.model

data class GoalItem(
    val title: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val isCompleted: Boolean
)
