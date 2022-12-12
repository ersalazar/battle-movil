package com.example.navalbattlegameapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.navalbattlegameapplication.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ScoresActivity : AppCompatActivity() {
    lateinit var llScores: LinearLayout
    lateinit var txtTop: TextView
    lateinit var btnBack: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scores)


        txtTop = findViewById<TextView>(R.id.txt_top)
        txtTop.text = "Puntaje - Iniciales"

        llScores = findViewById<LinearLayout>(R.id.linear_scores)

        val database = Firebase.database
        val msg = database.getReference("message")
        msg.setValue("Conectado a Firebase desde Scores!")

        val usersRef = database.getReference(USER)
        var userList = mutableListOf<User>()
        usersRef.get().addOnSuccessListener {
            val users = it.children
            for (user in users){
                val maxPoints = user.child(MAX_POINTS).value.toString()
                val username = user.child(USERNAME).value.toString()

                val userTemp = User(username = username, maxPoints = maxPoints.toInt())
                userList.add(userTemp)
            }
            userList.sortByDescending { it.maxPoints }
            for(user in userList){
                val tvGame = TextView(this)
                tvGame.text = "   ${user.maxPoints}   -   ${user.username}"
                tvGame.gravity = txtTop.gravity
                tvGame.textSize = 25f
                llScores.addView(tvGame)
            }
        }



        btnBack = findViewById<Button>(R.id.btn_regresar)
        btnBack.setOnClickListener{
            val act1 = Intent(this,MainActivity::class.java)
            startActivity(act1)
        }
    }
}
