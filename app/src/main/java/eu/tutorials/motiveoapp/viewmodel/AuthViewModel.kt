package eu.tutorials.motiveoapp.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore

class AuthViewModel : ViewModel() {

    private val auth = Firebase.auth //bunu yaptıktan sonra firestore veritabanı oluşturduk

    private val firestore = Firebase.firestore
    fun login(){

    }

    fun signup(){

    }

}