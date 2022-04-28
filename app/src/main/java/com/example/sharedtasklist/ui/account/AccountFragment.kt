package com.example.sharedtasklist.ui.account

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.sharedtasklist.R
import com.example.sharedtasklist.databinding.AccountFragmentBinding
import com.example.sharedtasklist.databinding.FragmentNotificationsBinding
import com.example.sharedtasklist.ui.notifications.NotificationsViewModel
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.util.*

class AccountFragment : Fragment() {

    private var _binding: AccountFragmentBinding? = null

    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

//    companion object {
//        fun newInstance() = AccountFragment()
//    }

//    private lateinit var viewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth

        val accountViewModel =
            ViewModelProvider(this).get(AccountViewModel::class.java)

        _binding = AccountFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val currentUser = auth.currentUser

        val textView: TextView = binding.tText
        val userName: TextView = binding.tUName
        val userEmail: TextView = binding.tUEmail
        val userImg: ImageView = binding.imgUser
        val btnPost: Button = binding.bPostStatus

        btnPost.setOnClickListener {
            postStatus()
        }

        userName.text = currentUser?.displayName.toString()
        userEmail.text = currentUser?.email.toString()
        Glide.with(this)
            .load(currentUser?.photoUrl)
            .placeholder(R.drawable.ic_baseline_person_24)
            .circleCrop()
            .into(userImg)

        accountViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }


        return root
    }

    private fun postStatus () {
        var db = FirebaseFirestore.getInstance()
        val postsRef = db.collection("posts")

        val etStatus: EditText = binding.etPostStatus



        val statusInfo = hashMapOf(
            "createdAt" to Timestamp(Date()),
            "createdBy" to auth.currentUser?.uid,
            "Status" to etStatus.text.toString(),
        )

        postsRef.add(statusInfo)

    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}