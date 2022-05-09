package com.example.sharedtasklist

import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtasklist.dataClasses.Conversation
import com.example.sharedtasklist.dataClasses.ConvoMessage
import com.example.sharedtasklist.recyclerViewAdapters.MessageAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_conversation.*
import kotlinx.android.synthetic.main.activity_conversation.et_Message

class ConversationActivity : AppCompatActivity() {
    val TAG = "CONVERSATION"

    private lateinit var auth: FirebaseAuth
    private lateinit var realDB: DatabaseReference
    private lateinit var adapter: MessageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversation)

        auth = Firebase.auth
        realDB = Firebase.database.reference

        val messagesRef = realDB.child("messages")

        val name = intent.getStringExtra("name").toString()
        val id = intent.getStringExtra("id").toString()
        val message = intent.getStringExtra("message").toString()

        setTitle(name)
        Log.d("CONVO", name)
        Log.d("CONVO", id)

        val messages = ArrayList<ConvoMessage>()

        val RV = findViewById<RecyclerView>(R.id.rv_Messages)
        val adapter = MessageAdapter(messages, auth.currentUser?.displayName.toString())
        val manager = LinearLayoutManager(this)
        RV.adapter = adapter
        RV.layoutManager = manager

        generateMessages(messages, id, adapter, RV)

        iv_SendButton.setOnClickListener {
            sendMessage(id)
        }

    }

    private fun generateMessages(
        M: ArrayList<ConvoMessage>,
        id: String,
        A: MessageAdapter,
        RV: RecyclerView
    ) {

        var initial = false

        val convoRef = realDB.child("messages/$id").limitToLast(1)

        realDB.child("messages/$id").get().addOnSuccessListener { messages ->
            if (messages.exists()) {
                messages.children.forEach { message ->

                    val name = message.child("name").value.toString()
                    val text = message.child("message").value.toString()
                    val message = ConvoMessage(name, text)
                    M.add(message)
//                    Log.d(TAG, message.child("name").value.toString())
//                    val test: HashMap<String, String> = message.value as HashMap<String, String>
//                    for (tests in test) {
//                        Log.d(TAG, tests.toString())
//                    }

                }

            } else {
                Log.d(TAG, "NOPE")
            }
            A.notifyDataSetChanged()
            RV.scrollToPosition(M.size-1)
            initial = true
        }


        val conversationListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists() && initial) {
                    snapshot.children.forEach { message ->
                        val name = message.child("name").value.toString()
                        val text = message.child("message").value.toString()
                        val message = ConvoMessage(name, text)
                        M.add(message)
                    }
                    A.notifyDataSetChanged()
                    RV.scrollToPosition(M.size-1)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(
                    com.example.sharedtasklist.ui.messages.TAG,
                    "loadPost:onCancelled",
                    databaseError.toException()
                )
            }
        }
        convoRef.addValueEventListener(conversationListener)

//        A.notifyDataSetChanged()
    }

    private fun sendMessage(id: String) {
        val message = et_Message.text
        if (message.isNotEmpty()) {
            val messageRef = realDB.child("messages/$id")
            val key = messageRef.push().key
            val name = auth.currentUser?.displayName

            if (key == null) {
                Log.w(TAG, "Couldn't get push key for messages")
                return
            }

            val messageInfo = hashMapOf(
                "name" to name.toString(),
                "message" to message.toString()
            )

            messageRef.child(key).setValue(messageInfo)
            message.clear()
        }
    }
}