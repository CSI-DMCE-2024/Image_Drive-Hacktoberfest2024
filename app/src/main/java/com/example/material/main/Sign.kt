package com.example.material.main

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import com.example.material.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase





class Sign : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign)

        var email=findViewById<TextInputEditText>(R.id.email_log)
        var pass=findViewById<TextInputEditText>(R.id.pass_log)
        var but=findViewById<Button>(R.id.button6)
        var auth=Firebase.auth
        var check=findViewById<CheckBox>(R.id.checkBox)

        //datastore
        val sharedPreferences= getSharedPreferences("counter", MODE_PRIVATE)



        but.setOnClickListener {
            var check1=check.isChecked


            var emailid=email.text.toString()
            var passid=pass.text.toString()
            if (emailid.isEmpty()||passid.isEmpty())
            {
                Toast.makeText(this, "FILL DETAILS", Toast.LENGTH_SHORT).show()
            }
            else {

                auth.signInWithEmailAndPassword(emailid, passid).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val userid=auth.uid
                        sharedPreferences.edit().apply{
                            putBoolean("flag",check1)
                            putString("uid",userid)
                        }.apply()

                        val user = auth.currentUser
                        startActivity(
                            Intent(
                                this,
                                MainActivity3::class.java
                            ).apply { putExtra("USER", user) })
                        Log.d(ContentValues.TAG, "signInWithEmail:success")
                    } else {
                        Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}