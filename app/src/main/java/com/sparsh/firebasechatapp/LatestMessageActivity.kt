package com.sparsh.firebasechatapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_message.*

class LatestMessageActivity : AppCompatActivity() {

    companion object{
        var currentUser : User? = null
    }
    private val adapter = GroupAdapter<ViewHolder>()

    val latestMessagesMap = HashMap<String,ChatMessage>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_message)

        RecyclerViewLatestMessage.adapter = adapter
        RecyclerViewLatestMessage.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))
        //set item click listener
        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this,ChatLogActivity::class.java)
            val row =  item as LatestMessageSingleRow

            intent.putExtra(NewMessageActivity.USER_KEY,row.chatPartnerUser)
            startActivity(intent)
        }

        listenForLatestMessages()
        fetchCurrentUser()


        //Checking user if he/she is logged in or not
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null) {
            val intent = Intent(this@LatestMessageActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun fetchCurrentUser(){
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                currentUser = snapshot.getValue(User::class.java)
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }

      private fun listenForLatestMessages(){

         val fromId = FirebaseAuth.getInstance().uid
         val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
         ref.addChildEventListener(object : ChildEventListener{
             override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                 //Adding items to Recycler View
                 val chatMessage = snapshot.getValue(ChatMessage::class.java) ?: return
                 latestMessagesMap[snapshot.key!!] = chatMessage
                 refreshRecyclerViewMessages()


             }

             override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                 val chatMessage = snapshot.getValue(ChatMessage::class.java) ?:return
                 latestMessagesMap[snapshot.key!!] = chatMessage
                 refreshRecyclerViewMessages()

             }

             override fun onChildRemoved(snapshot: DataSnapshot) {

             }

             override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

             }

             override fun onCancelled(error: DatabaseError) {

             }

         } )

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menures,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.newMessage ->{
                val intent = Intent(this@LatestMessageActivity,NewMessageActivity::class.java)
                startActivity(intent)
            }
            R.id.logout ->{
                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this@LatestMessageActivity,MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }

        return super.onOptionsItemSelected(item)
    }
    private fun refreshRecyclerViewMessages(){
        adapter.clear()
        latestMessagesMap.values.forEach{
            adapter.add(LatestMessageSingleRow(it))
        }
    }


}