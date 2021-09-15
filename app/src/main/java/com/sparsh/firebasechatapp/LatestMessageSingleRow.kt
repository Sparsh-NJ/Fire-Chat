package com.sparsh.firebasechatapp

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.latestmessage_single_row.view.*

class LatestMessageSingleRow(private val chatMessage : ChatMessage): Item<ViewHolder>() {
    var chatPartnerUser: User? = null
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.LatestMessage_latest_text.text = chatMessage.text

        val chatPartnerId: String

        if(chatMessage.fromId==FirebaseAuth.getInstance().uid){
            chatPartnerId = chatMessage.toId
        }
        else{
            chatPartnerId = chatMessage.fromId
}
        val ref = FirebaseDatabase.getInstance().getReference("/users/$chatPartnerId")
        ref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                chatPartnerUser = snapshot.getValue(User::class.java)
                viewHolder.itemView.LatestMessageName.text = chatPartnerUser?.username
                Picasso.get().load(chatPartnerUser?.profileImageUrl).into(viewHolder.itemView.LatestMessageImage)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })




    }

    override fun getLayout(): Int {
        return R.layout.latestmessage_single_row
    }

}