package com.example.app.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChronometerViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    var elapsedTime by mutableStateOf(0L)
        private set

    var isRunning by mutableStateOf(false)
        private set

    var isPaused by mutableStateOf(false)
        private set

    private var timerJob: Job? = null


    fun startTimer() {
        if (!isRunning) {
            isRunning = true
            isPaused = false
            timerJob = viewModelScope.launch {
                while (true) {
                    delay(1000)
                    elapsedTime++
                }
            }
        }
    }

    fun pauseTimer() {
        if (isRunning) {
            timerJob?.cancel()
            isPaused = true
            isRunning = false
        }
    }

    fun continueTimer() {
        if (isPaused) startTimer()
    }

    fun endTimer() {
        // sayacı durdur
        timerJob?.cancel()
        isRunning = false
        isPaused = false

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null && elapsedTime > 0) {
            // Firestore'a çalışma oturumunu kaydet
            val data = hashMapOf(
                "duration" to elapsedTime,                       // saniye
                "date" to Timestamp.now()
            )

            db.collection("users")
                .document(uid)
                .collection("sessions")
                .add(data)
                .addOnSuccessListener { println("✅ Süre Firestore'a kaydedildi") }
                .addOnFailureListener { e -> println("❌ Firestore hata: $e") }

            // XP güncelle
            updateXpAfterSession(uid, elapsedTime)
        }

        // sayacı sıfırla
        elapsedTime = 0L
    }

    private fun updateXpAfterSession(userId: String, elapsedSeconds: Long) {
        val sessionLength = 25*60 // 25 dakika = 1500 saniye
        val sessionsCompleted = (elapsedSeconds / sessionLength).toInt()

        if (sessionsCompleted <= 0) return

        val userRef = db.collection("users").document(userId)
        userRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                val currentXp = document.getLong("xp") ?: 0L
                val newXp = currentXp + sessionsCompleted
                userRef.update("xp", newXp)
                    .addOnSuccessListener { println("✅ XP güncellendi: $newXp") }
                    .addOnFailureListener { e -> println("❌ XP güncellenirken hata: $e") }
            }
        }.addOnFailureListener {
            println("❌ Kullanıcı verisi alınamadı: $it")
        }
    }
    var xp by mutableStateOf(0)
        private set

    private var xpListener: ListenerRegistration? = null

    fun startXpListener() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        xpListener = db.collection("users")
            .document(uid)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    println("❌ XP Listener error: $error")
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    xp = snapshot.getLong("xp")?.toInt() ?: 0
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        xpListener?.remove()
    }
}
