package com.example.snapchat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_create_snap.view.*


class MainActivity : AppCompatActivity() {

    var emailEditText : EditText ? = null
    var passwordEditText : EditText ? = null
    val mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)

        if (mAuth.currentUser != null) {
            logIn()
        }
    }

    fun goClicked(view :View) {

        Log.i(emailEditText?.text.toString(), passwordEditText?.text.toString())

        mAuth.signInWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                   logIn()
                } else {

                    mAuth.createUserWithEmailAndPassword(emailEditText?.text.toString(), passwordEditText?.text.toString())
                        .addOnCompleteListener(
                            this) {
                                task ->
                                if (task.isSuccessful) {
                                    //add to database
                                    //FirebaseDatabase.getInstance().getReference().child("users").child(task.result?.user?.uid!!).child("email").setValue(emailEditText?.text.toString())
                                    FirebaseDatabase.getInstance().getReference().child("users").child(task.result?.user?.uid.toString()).child("email").setValue(emailEditText?.text.toString())
                                    logIn()
                                } else {
                                    Toast.makeText(this,"Login Failed.Try Again.",Toast.LENGTH_SHORT).show()
                                }

                    }
                }

            }
    }

    fun logIn() {

        val intent = Intent(this,SnapsActivity::class.java)
        startActivity(intent)

    }
}

