package com.example.navalbattlegameapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import com.example.navalbattlegameapplication.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

const val USER = "User"
const val USERNAME = "username"
const val FULL_NAME = "fullname"
const val PASSWORD = "password"
const val ACTIVE = "active"
const val CURRENT_POINTS = "currentPoints"
const val MAX_POINTS = "maxPoints"
class LoginActivity : AppCompatActivity() {
    lateinit var llLogin: LinearLayout
    lateinit var inputEmail: EditText
    lateinit var inputPw: EditText
    lateinit var btnLogin: Button
    lateinit var txtTest: TextView
    lateinit var llSign: LinearLayout
    lateinit var inputFullName: EditText
    lateinit var inputUsernameS: EditText
    lateinit var inputEmailS: EditText
    lateinit var inputPwS: EditText
    lateinit var btnSignIn: Button
    lateinit var btnSign: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        llSign = findViewById(R.id.ll_signIn)
        llSign.visibility = GONE

        val db = Firebase.database
        val auth = Firebase.auth
        val myRef = db.reference

        val msg = db.getReference("message")
        msg.setValue("Conectado a Firebase desde el login blabla!")

        val usersRef = db.getReference(USER)


        inputEmail = findViewById(R.id.input_email)
        inputPw = findViewById(R.id.input_pw)
        txtTest = findViewById(R.id.txt_test)

        btnLogin = findViewById(R.id.btn_login)
        btnLogin.setOnClickListener {
            val email = inputEmail.text.toString()
            val password = inputPw.text.toString()
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    usersRef.get().addOnSuccessListener {
                        val users = it.children
                        for(user in users){
                            if(user.child("email").value.toString() == email){
                                val username = user.child(USERNAME).value.toString()
                                usersRef.child(username).child(ACTIVE).setValue(true)
                                break
                            }
                        }
                    }
                    Toast.makeText(applicationContext, "Authentication success.", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser

                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Authentication failed. "+ task.exception, Toast.LENGTH_SHORT).show()
                }
            }
        }

        inputFullName = findViewById(R.id.inputS_fullName)
        inputUsernameS = findViewById(R.id.inputS_username)
        inputEmailS = findViewById(R.id.inputS_email)
        inputPwS = findViewById(R.id.inputS_pw)
        btnSignIn = findViewById(R.id.btnS_signIn)

        btnSignIn.setOnClickListener{
            val fullName = inputFullName.text.toString()
            val username = inputUsernameS.text.toString()
            val email = inputEmailS.text.toString()
            val pw = inputPwS.text.toString()
            val newUser = User(fullName, username, email, pw,true,0,0)
            usersRef.child(username).setValue(newUser)

            auth.createUserWithEmailAndPassword(email, pw).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    usersRef.child(username).child(ACTIVE).setValue(true)
                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(applicationContext, ""+task.exception,Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage,Toast.LENGTH_LONG).show()
            }

        }

        llLogin = findViewById(R.id.ll_login)
        btnSign = findViewById(R.id.btn_signIn)
        btnSign.setOnClickListener{
            llLogin.visibility = GONE
            llSign.visibility = VISIBLE
        }

        usersRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
               // val user = snapshot.getValue<User>()

            }

            override fun onCancelled(error: DatabaseError) {
                // Log.w(TAG, "Failed to read value.", error.toException())
            }

        })

    }

}