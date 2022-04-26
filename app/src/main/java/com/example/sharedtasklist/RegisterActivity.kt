package com.example.sharedtasklist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = Firebase.auth
    }

    fun clickComplete(view: View) {
        var email = et_RegEmail.text.toString()
        var pass1 = et_RegPassword.text.toString()
        var pass2 = et_RegConPassword.text.toString()

        if (email.isNotEmpty() && pass1.isNotEmpty() && pass2.isNotEmpty()){
            if (pass1 == pass2) {
                createAccount(email, pass1)
            } else
                Toast.makeText(baseContext, "Passwords do not match", Toast.LENGTH_SHORT).show()

        } else
            Toast.makeText(baseContext, "Please fill in empty fields", Toast.LENGTH_SHORT).show()
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    startHomeActivity()
                    val user = auth.currentUser
                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    private fun startHomeActivity() {
        val myIntent = Intent(this, HomeActivity::class.java)
        startActivity(myIntent)
    }
}