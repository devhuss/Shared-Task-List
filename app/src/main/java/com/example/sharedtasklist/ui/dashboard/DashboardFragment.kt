package com.example.sharedtasklist.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtasklist.databinding.FragmentDashboardBinding
import com.example.sharedtasklist.databinding.FragmentMessagesBinding
import com.example.sharedtasklist.recyclerViewAdapters.ConversationAdapter
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class User(
    val name: String = "",
    val Status : String = ""
)

class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

class DashboardFragment : Fragment() {

    private companion object{
        private const val TAG = "DashboardFragment"
    }
    private lateinit var auth: FirebaseAuth


    val db = Firebase.firestore
    private var _binding: FragmentDashboardBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        auth = Firebase.auth
//        val query = db.collection("users")
//        val options = FirestoreRecyclerOptions.Builder<User>().setQuery(query, User::class.java)
//            .setLifecycleOwner(this).build()
//
//        val adapter = object: FirestoreRecyclerAdapter<User, UserViewHolder>(options){
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
//               val view = LayoutInflater.from(this@DashboardFragment).inflate(android.R.layout.activity_list_item, parent, false,)
//            }
//
//            override fun onBindViewHolder(holder: UserViewHolder, position: Int, model: User) {
//                TODO("Not yet implemented")
//            }
//
//        }
//
//       // return root
//    }

}
