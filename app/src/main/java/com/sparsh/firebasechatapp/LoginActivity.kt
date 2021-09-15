package com.sparsh.firebasechatapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var email:EditText
    private lateinit var password:EditText
    private lateinit var login:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        email = findViewById(R.id.loginUsername)
        password = findViewById(R.id.loginPassword)
        login = findViewById(R.id.btnLogin)

        login.setOnClickListener {
            val validEmail = email.text.toString()
            val validPassword = password.text.toString()

            if(validEmail.isEmpty() || validPassword.isEmpty())
                return@setOnClickListener

            FirebaseAuth.getInstance().signInWithEmailAndPassword(validEmail,validPassword).addOnCompleteListener{
                if(!it.isSuccessful)
                    return@addOnCompleteListener
                else {
                    Toast.makeText(this, "logged in successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity,LatestMessageActivity::class.java)

                    //used to clear stack and add new activity to stack
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
            }.addOnFailureListener{
                Toast.makeText(this, "invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }
}