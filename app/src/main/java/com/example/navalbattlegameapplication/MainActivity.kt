package com.example.navalbattlegameapplication

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

        val usersRef = database.getReference(USER)
        usersRef.get().addOnSuccessListener {
            var users = it.children
            Toast.makeText(this, it.key.toString(), Toast.LENGTH_LONG).show()
            for(user in users){
               // txtPlay.text = user.child(ACTIVE).value.toString()
               if(user.child(ACTIVE).value == true){
                   username = user.child(USERNAME).value.toString()
                   txtPlay.text = "Bienvenido $username"
                   txtLogin.text = "No eres tu? Inicia sesion o crea una cuenta!"
                   btnPlay.setBackgroundColor(Color.GREEN)
                   btnPlay.isEnabled = true
               }
            }
        }
        //val username = intent?.getStringExtra(USERNAME)


        btnLogin.setOnClickListener{
            val login = Intent(this, LoginActivity::class.java)
            if(username!=""){
                usersRef.child(username).child(ACTIVE).get().addOnSuccessListener {
                    if(it.value == true){
                        usersRef.child(username).child(ACTIVE).setValue(false)
                    }
                }
            }

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