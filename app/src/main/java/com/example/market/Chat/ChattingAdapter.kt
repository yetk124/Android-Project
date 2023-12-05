package com.example.market.Home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.market.Chat.Chatting
import com.example.market.Chat.FragmentChatting
import com.example.market.Chat.chatView
import com.example.market.R

class ChattingAdapter(
    private val sentence: FragmentChatting,
    private var chatList: List<Chatting>,
) : RecyclerView.Adapter<chatView>() {


    override fun getItemCount() = chatList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): chatView {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return chatView(view)
    }
    override fun onBindViewHolder(holder: chatView, position: Int) {
        val chat = chatList[position]
        Glide.with(sentence).load(chat.purchase)
        holder.purchaseTextView.text = chat.purchase
        holder.messageTextView.text = chat.message
    }

   

    fun setChats(chats: List<Chatting>) {
        chatList = chats
        notifyDataSetChanged()
    }
}
