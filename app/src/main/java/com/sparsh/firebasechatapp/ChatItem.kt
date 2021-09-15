package com.sparsh.firebasechatapp

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.chat_row1.view.*

class ChatItem(val text:String,var user: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.myMessage1.text = text
        val uri = user.profileImageUrl
        Picasso.get().load(uri).into(viewHolder.itemView.chatImage)

    }

    override fun getLayout(): Int {
        return R.layout.chat_row1

    }

}