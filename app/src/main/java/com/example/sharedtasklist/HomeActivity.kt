package com.example.sharedtasklist

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.sharedtasklist.databinding.ActivityHome2Binding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHome2Binding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHome2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_home2)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    fun signOut(view: View) {
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        // Make sure to call finish(), otherwise the user would be able to go back to the MainActivity
        finish()
    }
}