package eu.tutorials.motiveoapp

import android.content.Context
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

object AppUtil {

    fun showToast(context: Context,message : String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }

    fun addItemToGoals(){
        val userDoc = Firebase.firestore.collection("users")
            .document(FirebaseAuth.getInstance().currentUser?.uid!!)
    }
}