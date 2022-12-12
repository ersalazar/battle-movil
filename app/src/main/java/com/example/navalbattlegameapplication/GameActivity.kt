package com.example.navalbattlegameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class GameActivity : AppCompatActivity() {
    lateinit var btnFinish: Button

    lateinit var db: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
//        val bundle = intent.extras
//        if(bundle != null){
//            val username = bundle?.getString(USERNAME)
//        }

        db = Firebase.database
        val usersRef = db.getReference(USER)

        btnFinish = findViewById(R.id.btn_finish)
        btnFinish.setOnClickListener {
            var scores = Intent(this, ScoresActivity::class.java)
            startActivity(scores)
        }




//#region puntaje
//        usersRef.get().addOnSuccessListener {
//            var users = it.children
//            for (user in users){
//                var userName = username
//                var points = user.child(CURRENT_POINTS).value.toString().toInt()
//                var maxPoints = user.child(MAX_POINTS).value.toString().toInt()
//                if (points > maxPoints){
//                    if (userName != null) {
//                        usersRef.child(userName).child(MAX_POINTS).setValue(points)
//                    }
//
//                }
//            }
//        }
//#endregion
    }
}
