package com.sparsh.firebasechatapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var register: Button
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var login: TextView
    private lateinit var addPhoto: ImageView
    private lateinit var username:EditText
    private lateinit var cropImage:CircleImageView

    private var selectedUri :Uri ?= null
    private var imageURL= ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        email = findViewById(R.id.Email)
        password = findViewById(R.id.password)
        register = findViewById(R.id.Register)
        login = findViewById(R.id.Login)
        addPhoto = findViewById(R.id.imgChatLogo)
        username = findViewById(R.id.username)
        cropImage = findViewById(R.id.cropImage)

        addPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)

        }
        login.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        register.setOnClickListener {
            val validEmail = email.text.toString()
            val validPassword = password.text.toString()

            if (validEmail.isEmpty() || validPassword.isEmpty()  || selectedUri == null) {
                Toast.makeText(this, "please fill the required fields\nincluding the profile picture", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            FirebaseAuth.getInstance().createUserWithEmailAndPassword(validEmail,validPassword).addOnCompleteListener{
                if(!it.isSuccessful) {
                    return@addOnCompleteListener

                }
                else {
                    Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show()
                    saveData()
                    val intent = Intent(this@MainActivity,LatestMessageActivity::class.java)

                    //used to clear stack and add new activity to stack
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)

                    startActivity(intent)
                }

            }.addOnFailureListener{
                Toast.makeText(this, "invalid email", Toast.LENGTH_SHORT).show()
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,selectedUri)
            cropImage.setImageBitmap(bitmap)
            addPhoto.alpha = 0f

            //Uploading image to firebaseStorage
            val filename = UUID.randomUUID().toString()
            if(selectedUri == null) return
            val ref =  FirebaseStorage.getInstance().getReference("/images/$filename")
            ref.putFile(selectedUri!!).addOnSuccessListener {

                //Location of image
                ref.downloadUrl.addOnSuccessListener {
                    imageURL = it.toString()
                }
            }

        }

    }

    //Uploading data to FirebaseDatabase

    private fun saveData() {

        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref2 = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, username.text.toString(), imageURL)
        ref2.setValue(user)

    }
}
@Parcelize
data class User(val uid:String,val username :String, var profileImageUrl:String):Parcelable{
    constructor():this("","","")

}