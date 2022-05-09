package com.example.sharedtasklist.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtasklist.HomeActivity
//import com.example.sharedtasklist.User
import com.example.sharedtasklist.databinding.FragmentHomeBinding
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home2.*
import kotlinx.android.synthetic.main.fragment_home.*

data class User(
    val Name: String = "",
    val Status : String = ""
)
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    val db = Firebase.firestore

    private companion object{
        private const val TAG = "HomeFragment"
    }
    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)


        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val rvUsers: RecyclerView = binding.rvUsers

        val query = db.collection("posts")
        val options = FirestoreRecyclerOptions.Builder<User>().setQuery(query, User::class.java)
            .setLifecycleOwner(this).build()
        val adapter = object: FirestoreRecyclerAdapter<User, UserViewHolder>(options){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
                val view =  LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)
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
        rvUsers.layoutManager = LinearLayoutManager(context)


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}