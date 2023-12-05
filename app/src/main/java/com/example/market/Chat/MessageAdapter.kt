package com.example.market.Chat

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.market.R

class MessageAdapter(private var messages: List<Message>) :
    RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {


    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val message = messages[position]
        holder.bind(message)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user, parent, false)
        return MessageViewHolder(view)
    }



    fun setMessages(messages: List<Message>) {
        this.messages = messages
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageTextView: TextView = itemView.findViewById(R.id.messageTextView)
        private val purchaseTextView: TextView = itemView.findViewById(R.id.purchaseTextView)


        fun bind(message: Message) {
            purchaseTextView.text = message.purchase
            messageTextView.text = message.message
        }
    }
}
