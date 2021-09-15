package com.sparsh.firebasechatapp

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_row2.view.*

class ChatItem2(val text:String, private val user:User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.myMessage2.text = text

        val uri = user.profileImageUrl
        Picasso.get().load(uri).into(viewHolder.itemView.chatImage2)

    }

    override fun getLayout(): Int {
        return R.layout.chat_row2
    }

}