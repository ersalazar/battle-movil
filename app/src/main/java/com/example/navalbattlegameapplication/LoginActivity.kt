package com.example.navalbattlegameapplication

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*
import com.example.navalbattlegameapplication.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.database.ktx.snapshots
import com.google.firebase.ktx.Firebase
import java.lang.ref.Reference

const val USER = "User"
const val USERNAME = "username"
const val PASSWORD = "password"
const val ACTIVE = "active"
class LoginActivity : AppCompatActivity() {
    lateinit var llLogin: LinearLayout
    lateinit var inputUsername: EditText
    lateinit var inputPw: EditText
    lateinit var btnLogin: Button
    lateinit var txtTest: TextView
    lateinit var llSign: LinearLayout
    lateinit var inputFullName: EditText
    lateinit var inputUsernameS: EditText
    lateinit var inputEmail: EditText
    lateinit var inputPwS: EditText
    lateinit var btnSignIn: Button
    lateinit var btnSign: Button
    private lateinit var usersRef: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        llSign = findViewById(R.id.ll_signIn)
        llSign.visibility = GONE

        val db = Firebase.database
        val msg = db.getReference("message")
        msg.setValue("Conectado a Firebase desde el login!")

        usersRef = db.getReference(USER)

        inputUsername = findViewById(R.id.input_username)
        inputPw = findViewById(R.id.input_pw)
        txtTest = findViewById(R.id.txt_test)
        btnLogin = findViewById(R.id.btn_login)
        btnLogin.setOnClickListener {
            val username = inputUsername.text.toString()
            val pw = inputPw.text.toString()

            usersRef.child(username).get().addOnSuccessListener {
                if(it.exists()){
                    val userPw = it.child(PASSWORD).value.toString()
                    if(pw == userPw){
                        usersRef.child(username).child(ACTIVE).setValue(true)
                        Toast.makeText(this, "Bienvenido, $username", Toast.LENGTH_LONG)
                        var main = Intent(this, MainActivity::class.java).putExtra(USERNAME, username)
                        startActivity(main)

                    }else{
                        Toast.makeText(this, "Contrasena incorrecta! Intenta de nuevo", Toast.LENGTH_LONG)
                    }
                }else{
                    txtTest.text = "Usuario no existente! Intenta de nuevo"
                }
            }.addOnFailureListener{
                Toast.makeText(this, "firebase Error getting data $it", Toast.LENGTH_LONG).show()
            }
        }


        inputFullName = findViewById(R.id.inputS_fullName)
        inputUsernameS = findViewById(R.id.inputS_username)
        inputEmail = findViewById(R.id.inputS_email)
        inputPwS = findViewById(R.id.inputS_pw)
        btnSignIn = findViewById(R.id.btnS_signIn)

        btnSignIn.setOnClickListener{
            val fullName = inputFullName.text.toString()
            val username = inputUsernameS.text.toString()
            val email = inputEmail.text.toString()
            val pw = inputPwS.text.toString()

            val newUser = User(fullName, username, email, pw)

            usersRef.child(username).setValue(newUser).addOnSuccessListener {

//                inputFullName.text.clear()
//                inputUsernameS.text.clear()
//                inputEmail.text.clear()
//                inputPwS.text.clear()

                llSign.visibility = GONE
                llLogin.visibility = VISIBLE
                Toast.makeText(this, "Ya puedes iniciar sesion!", Toast.LENGTH_LONG).show()

            }.addOnFailureListener{
                Toast.makeText(this, "Algo salio mal, vuelve a intentar", Toast.LENGTH_LONG).show()
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
                val user = snapshot.getValue<User>()
                Toast.makeText(applicationContext, user.toString(), Toast.LENGTH_LONG)
            }

            override fun onCancelled(error: DatabaseError) {
                // Log.w(TAG, "Failed to read value.", error.toException())
            }

        })

    }

}