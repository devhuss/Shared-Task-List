package com.example.sharedtasklist.ui.messages

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtasklist.R
import com.example.sharedtasklist.dataClasses.Conversation
import com.example.sharedtasklist.databinding.FragmentMessagesBinding
import com.example.sharedtasklist.recyclerViewAdapters.ConversationAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

val TAG = "MESSAGES"

class MessagesFragment : Fragment() {


    private var _binding: FragmentMessagesBinding? = null

    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser: FirebaseUser
    private lateinit var fireDB: FirebaseFirestore
    private lateinit var realDB: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        auth = Firebase.auth
        currentUser = auth.currentUser!!
        fireDB = FirebaseFirestore.getInstance()
        realDB = Firebase.database.reference

        val pFragment = parentFragmentManager

        _binding = FragmentMessagesBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val conversations = ArrayList<Conversation>()

        val recyclerView = binding.rvConversations
        val conversationAdapter = ConversationAdapter(conversations, context)
        recyclerView.adapter = conversationAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)

        generateConversation(conversations, conversationAdapter)

        val newConvoButton: FloatingActionButton = binding.fabNewConvo

        val et_ConvoEmail = EditText(context)



        val dialog: AlertDialog = AlertDialog.Builder(context)
            .setTitle("Create new conversation")
            .setMessage("Enter user email:")
            .setView(et_ConvoEmail)
            .setPositiveButton("OK") { dialog, which ->
                createConvo(et_ConvoEmail.text.toString())
            }
            .setNegativeButton("Cancel", null)
            .create()

        newConvoButton.setOnClickListener {
            dialog.show()
        }


        return root
    }

    private fun generateConversation( C: ArrayList<Conversation>, CA: ConversationAdapter) {

        val userConvoRef = realDB.child("users").child(currentUser.uid).child("conversations")
        val convoListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                C.clear()
                if (snapshot.exists()) {
                    val convos: HashMap<String, Boolean> =
                        snapshot.getValue() as HashMap<String, Boolean>
                    for (convo in convos) {

                        realDB.child("members/${convo.key}").get().addOnSuccessListener {
                            val members: HashMap<String,Boolean> = it.value as HashMap<String, Boolean>
                            for (member in members) {
                                fireDB.collection("users")
                                    .whereEqualTo("id", member.key)
                                    .get()
                                    .addOnCompleteListener { docs ->
                                        for (doc in docs.result){
                                            if (doc.data["id"] != currentUser.uid) {
                                                Log.d(TAG, convo.key)
                                                val name = doc.data["name"].toString()
                                                val id = convo.key
                                                val text = "text"
                                                val img = R.drawable.ic_baseline_person_24
                                                val conversation = Conversation(name,id , text, img)
                                                C.add(conversation)
                                            }
                                        }
                                        CA.notifyDataSetChanged()
                                    }
                            }

                        }


                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }

        }
        userConvoRef.addValueEventListener(convoListener)
        Log.d("END", C.toString())


    }

    private fun createConvo(email: String) {
        fireDB.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    for (document in documents) {
                        addConvoInDatabase(document.id)
                    }
                } else {
                    val dialog: AlertDialog = AlertDialog.Builder(context)
                        .setTitle("User does not exist")
                        .setMessage("Please enter an existing user to create a conversation")
                        .setPositiveButton("OK", null)
                        .create()
                    dialog.show()
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }

    private fun addConvoInDatabase(user: String) {
        val key = realDB.child("chats").push().key

        val currUserID = currentUser.uid.toString()

        if (key == null) {
            Log.w(TAG, "Couldn't get push key for convo")
            return
        }

        val memberInfo = hashMapOf(
            currUserID to true,
            user to true
        )

        val convoInfo = hashMapOf(
            "latestMessage" to ""
        )

        val databaseUpdate = hashMapOf<String, Any>(
            "members/$key" to memberInfo,
            "chats/$key" to convoInfo,
            "users/$currUserID/conversations/$key" to true,
            "users/$user/conversations/$key" to true,
        )

        realDB.updateChildren(databaseUpdate)

    }

}