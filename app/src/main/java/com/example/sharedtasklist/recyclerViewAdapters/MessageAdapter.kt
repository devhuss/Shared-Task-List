
package com.example.sharedtasklist.recyclerViewAdapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.RecyclerView
import com.example.sharedtasklist.R
import com.example.sharedtasklist.dataClasses.ConvoMessage

class MessageAdapter(private val messages: ArrayList<ConvoMessage>, private val userName: String): RecyclerView.Adapter<MessageAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val messageName : TextView = itemView.findViewById(R.id.tv_MName)
        val messageText : TextView = itemView.findViewById(R.id.tv_MMessage)
        val CL: RelativeLayout = itemView.findViewById(R.id.cl_Message)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message, null)
//        view.setBackgroundResource(R.drawable.rounded_message_blue)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = messages[position]
        holder.messageName.text = currentItem.name
        holder.messageText.text = currentItem.text

//        val tester: Int = holder.messageName as Int

        val tParams = holder.messageText.layoutParams as RelativeLayout.LayoutParams
        val nParams = holder.messageName.layoutParams as RelativeLayout.LayoutParams

        if (currentItem.name == userName) {

            holder.messageText.setBackgroundResource(R.drawable.rounded_message_blue)

            tParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            nParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        } else {
            holder.messageText.setBackgroundResource(R.drawable.rounded_message_gray)
            tParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            nParams.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }


}