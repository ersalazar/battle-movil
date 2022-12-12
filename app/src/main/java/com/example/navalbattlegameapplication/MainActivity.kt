package com.example.navalbattlegameapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var btnLogin: Button
    lateinit var btnPlay: Button
    lateinit var txtPlay: TextView
    lateinit var txtLogin: TextView
    //lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actitity_main)

        txtPlay = findViewById(R.id.txt_play)
        txtLogin = findViewById(R.id.txt_login)
        btnPlay  = findViewById(R.id.btn_play)
        btnLogin = findViewById(R.id.btn_login)
        var username = ""
        btnPlay.setBackgroundColor(Color.GRAY)
        btnPlay.isEnabled = false

        //Connect de database
        val database = Firebase.database
        val msg = database.getReference("message")
        msg.setValue("Conectado a Firebase!")

        val auth = Firebase.auth

        val user = auth.currentUser
        if(user != null){
            val usersRef = database.getReference(USER)
            usersRef.get().addOnSuccessListener {

            }
            txtPlay.text = "Bienvenido ${user.displayName}"
            txtLogin.text = "No eres tu? Inicia sesion o crea una cuenta!"
            btnPlay.setBackgroundColor(Color.GREEN)
            btnPlay.isEnabled = true
        }
        user?.let {
            // Name, email address, and profile photo Url
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            // Check if user's email is verified
            val emailVerified = user.isEmailVerified

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getToken() instead.
            val uid = user.uid
        }




        btnLogin.setOnClickListener{
            val usersRef = database.getReference(USER)
            usersRef.get().addOnSuccessListener {
                var users = it.children
                for(user in users){
                    // txtPlay.text = user.child(ACTIVE).value.toString()
                    if(user.child(ACTIVE).value == true){
                        val username = user.child(USERNAME).value.toString()
                        usersRef.child(username).child(ACTIVE).setValue(false)
                    }
                }
            }
            auth.signOut()
            val login = Intent(this, LoginActivity::class.java)
            startActivity(login)
        }

        btnPlay.setOnClickListener{
            val game = Intent(this, GameActivity::class.java)
            startActivity(game)
        }


        // Read from the database
        msg.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.getValue<String>()
                //Toast.makeText(applicationContext, value, Toast.LENGTH_SHORT).show()
                //Log.d(TAG, "Value is: " + value)
            }

            override fun onCancelled(error: DatabaseError) {
               // Log.w(TAG, "Failed to read value.", error.toException())
            }

        })
    }
}