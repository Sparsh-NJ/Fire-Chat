package com.sparsh.firebasechatapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class NewMessageActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var layout: RecyclerView.LayoutManager

    companion object {
        const val USER_KEY = "USER_KEY"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        title = "Select User"
        val adapter = GroupAdapter<ViewHolder>()

        recyclerView = findViewById(R.id.RecyclerView)
        layout = LinearLayoutManager(this)


        recyclerView.layoutManager = layout


        // EXtracting users from Firebase


        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {


                snapshot.children.forEach {

                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        recyclerView.adapter = adapter
                        adapter.add(UserItem(user))
                    }
                }


                adapter.setOnItemClickListener { item, view ->
                    val userItem = item as UserItem
                    val intent = Intent(view.context, ChatLogActivity::class.java)
                    intent.putExtra(USER_KEY, userItem.user)
                    startActivity(intent)
                    finish()
                }


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

}

