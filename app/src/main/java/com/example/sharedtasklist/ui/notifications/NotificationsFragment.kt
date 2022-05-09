package com.example.sharedtasklist.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.sharedtasklist.databinding.FragmentNotificationsBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_notifications.*

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val notificationsViewModel =
            ViewModelProvider(this).get(NotificationsViewModel::class.java)

        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = "Test"
        }

        val database = Firebase.database.reference
        val data = database.child("test").child("members").child("2").child("10").setValue("Test")
//            .addOnSuccessListener {
//                Log.i("FIREBASE", "Got value $it")
//            }.addOnFailureListener {
//            Log.e("firebase", "Error getting data", it)
//        }
//        Log.d("NOTIFY", data.toString())

//        myRef.setValue("Hello, Test!")

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}