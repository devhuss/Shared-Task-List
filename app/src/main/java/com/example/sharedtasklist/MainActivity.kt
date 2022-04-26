package com.example.sharedtasklist

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

const val TAG = "MAIN ACTIVITY"



class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = Firebase.auth

        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
            //AuthUI.IdpConfig.PhoneBuilder().build(),
            //AuthUI.IdpConfig.FacebookBuilder().build(),
            //AuthUI.IdpConfig.TwitterBuilder().build()
        )


        var db = FirebaseFirestore.getInstance()
        val usersRef = db.collection("users")

        val docData = hashMapOf(
            "stringExample" to "Hello world!",
            "booleanExample" to true,
            "numberExample" to 3.14159265,
            "dateExample" to Timestamp(Date()),
            "listExample" to arrayListOf(1, 2, 3),
            "nullExample" to null
        )

        usersRef.document("one")
            .set(docData)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        Log.d(TAG, docData.toString())
    }

    fun clickSignIn(view: View) {
        var email = et_Email.text.toString()
        var password = et_Password.text.toString()

        if (email.isNotEmpty() || password.isNotEmpty())
            signIn(email, password)
        else
            Toast.makeText(baseContext, "Please enter Email or Password", Toast.LENGTH_SHORT).show()
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser?.uid

        t_ID.text = currentUser.toString()
        if(currentUser != null){
            //reload();
        }
    }

    private fun createAccount(email: String, password: String) {
        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END create_user_with_email]
    }

    private fun signIn(email: String, password: String) {
        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
        // [END sign_in_with_email]
    }

    fun signOut(view: View) {
        auth.signOut()

    }

    fun homeClick(view: View) {
        val myIntent = Intent(this, HomeActivity::class.java)
        startActivity(myIntent)

    }

    private fun updateUI(user: FirebaseUser?) {
        t_ID.text = user?.uid.toString()
//        user?.getIdToken(true)?.addOnSuccessListener(this) { tokenResult ->
//            val tokenID = tokenResult.token
//            Log.d("TOKEN ID 1", tokenID.toString())
//        }
//        val mUser = FirebaseAuth.getInstance().currentUser
//        mUser!!.getIdToken(true)
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val idToken: String? = task.result.token
//                    Log.d("TOKEN ID 2", idToken.toString())
//                    // Send token to your backend via HTTPS
//                    // ...
//                } else {
//                    // Handle error -> task.getException();
//                }
//            }
    }

    private fun reload() {

    }
}