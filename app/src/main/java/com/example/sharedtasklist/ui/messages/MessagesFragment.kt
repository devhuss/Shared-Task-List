package com.example.sharedtasklist.ui.messages

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtasklist.R
import com.example.sharedtasklist.dataClasses.Conversation
import com.example.sharedtasklist.databinding.FragmentMessagesBinding
import com.example.sharedtasklist.recyclerViewAdapters.ConversationAdapter

class MessagesFragment : Fragment() {

    private var _binding: FragmentMessagesBinding? = null

    private val binding get() = _binding!!

//    companion object {
//        fun newInstance() = MessagesFragment()
//    }
//
//    private lateinit var viewModel: MessagesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val messagesViewModel = ViewModelProvider(this).get(MessagesViewModel::class.java)

        _binding = FragmentMessagesBinding.inflate(inflater, container, false)

        val root: View = binding.root

        val recyclerView = binding.rvConversations
        recyclerView.adapter = ConversationAdapter(generateConversation(20))
        recyclerView.layoutManager = LinearLayoutManager(context)

//        messagesViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }

        return root
    }

    private fun generateConversation(size: Int): ArrayList<Conversation> {
        val conversations = ArrayList<Conversation>()

        for (i in 1..size) {
            val convo = Conversation("John Doe-$i", "text $i", R.drawable.ic_baseline_person_24)
            conversations.add(convo)
        }
        // return the list of contacts
        return conversations
    }

//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(MessagesViewModel::class.java)
//        // TODO: Use the ViewModel
//    }

}