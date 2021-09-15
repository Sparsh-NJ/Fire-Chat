package com.sparsh.firebasechatapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import java.util.*

class ChatLogActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val adapter = GroupAdapter<ViewHolder>()
        val layoutManager = LinearLayoutManager(this)

        RecyclerViewChatLog.adapter = adapter
        RecyclerViewChatLog.layoutManager = layoutManager


        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        if (user != null) {
            title = user.username
        }

        val fromIdd = FirebaseAuth.getInstance().uid
        val toIdd = user?.uid

        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromIdd/$toIdd")

        ref.addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if(chatMessage!=null) {
                    if(chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val sender = LatestMessageActivity.currentUser
                        adapter.add(ChatItem(chatMessage.text,sender!!))
                    }
                    else{
                        val receiver = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
                        adapter.add(ChatItem2(chatMessage.text,receiver!!))
                    }
                }
                RecyclerViewChatLog.scrollToPosition(adapter.itemCount - 1)

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

        SendMessage.setOnClickListener {
            val text = EnterMessage.text.toString()
            val fromId = FirebaseAuth.getInstance().uid
            val toId = user?.uid

            if (fromId == null) return@setOnClickListener
            if (toId == null) return@setOnClickListener

            //val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
            val reference =
                FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
            val toReference =
                FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
            val chatMessage = ChatMessage(text,
                reference.key!!,
                fromId,
                toId,
                System.currentTimeMillis() / 1000)
            reference.setValue(chatMessage).addOnSuccessListener {
                EnterMessage.text.clear()
                RecyclerViewChatLog.scrollToPosition(adapter.itemCount - 1)
            }

            toReference.setValue(chatMessage)

            val latestMessageRef =
                FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
            latestMessageRef.setValue(chatMessage)

            val latestMessageRef2 =
                FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
            latestMessageRef2.setValue(chatMessage)
        }

    }
}
