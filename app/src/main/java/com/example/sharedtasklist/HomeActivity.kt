package com.example.sharedtasklist

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtasklist.databinding.ActivityHome2Binding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home2.*
import kotlinx.android.synthetic.main.fragment_home.*

data class User(
    val Name: String = "",
    val Status : String = ""
)

class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


class HomeActivity : AppCompatActivity() {
    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    private lateinit var binding: ActivityHome2Binding
    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore

    private companion object{
        private const val TAG = "HomeActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHome2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.rvUsers)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_dashboard, R.id.navigation_notifications, R.id.navigation_account
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val query = db.collection("posts")
        val options = FirestoreRecyclerOptions.Builder<User>().setQuery(query, User::class.java)
            .setLifecycleOwner(this).build()
        val adapter = object: FirestoreRecyclerAdapter<User, UserViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
               val view =  LayoutInflater.from(this@HomeActivity).inflate(android.R.layout.simple_list_item_2, parent, false)
                return UserViewHolder(view)
            }

            override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
                val tvName: TextView = holder.itemView.findViewById(android.R.id.text1)
                val tvStatus: TextView = holder.itemView.findViewById(android.R.id.text2)
                tvName.text = model.Name
                tvStatus.text = model.Status

            }

        }
        rvUsers.adapter = adapter
        rvUsers.layoutManager = LinearLayoutManager(this)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                // User chose the "logout" item, logout the user then
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show()

                signOut()
                true
            }
            //R.id
            else -> {
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun signOut() {
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}