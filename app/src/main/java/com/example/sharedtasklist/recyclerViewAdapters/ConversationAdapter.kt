package com.example.sharedtasklist.recyclerViewAdapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtasklist.R
import com.example.sharedtasklist.dataClasses.Conversation

class ConversationAdapter(private val conversations: ArrayList<Conversation>): RecyclerView.Adapter<ConversationAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val converName: TextView = itemView.findViewById<TextView>(R.id.tv_conversationName)
        val converMessage: TextView = itemView.findViewById<TextView>(R.id.tv_conversationMessage)
        val converImage: ImageView = itemView.findViewById<ImageView>(R.id.iv_conversationImage)
    }

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.conversation, null)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = conversations[position]
        holder.converName.text = currentItem.name
        holder.converMessage.text = currentItem.latestMessage
        holder.converImage.setImageResource(currentItem.profileImage)
    }

    override fun getItemCount(): Int {
        return conversations.size
    }

}