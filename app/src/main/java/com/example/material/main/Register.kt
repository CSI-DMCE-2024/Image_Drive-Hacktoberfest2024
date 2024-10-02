package com.example.material.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.example.material.R
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val name = findViewById<TextInputEditText>(R.id.name)
        val email = findViewById<TextInputEditText>(R.id.email)
        val number = findViewById<TextInputEditText>(R.id.number)
        val pass = findViewById<TextInputEditText>(R.id.pass)
        var bu = findViewById<Button>(R.id.button5)
        val spinner=findViewById<AutoCompleteTextView>(R.id.gender)

        var genders= arrayListOf("MALE","FEMALE","TRANSGENDER")
        val adapter=ArrayAdapter(this,android.R.layout.simple_spinner_dropdown_item,genders)
        spinner.setAdapter(adapter)



        auth = Firebase.auth

        bu.setOnClickListener {

            var name1 = name.text.toString()
            var email1 = email.text.toString()
            var number1 = number.text.toString()
            var pass1 = pass.text.toString()
            var gender=spinner.text.toString()
            if (name1.isEmpty()|| email1.isEmpty() || number1.isEmpty()|| pass1.isEmpty()) {
                Toast.makeText(this, "ALL DETAILS REQUIRED", Toast.LENGTH_SHORT).show()
            } else if (pass1.length < 6) {
                Toast.makeText(this, "PASSWORD TOO SHORT", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email1, pass1).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //RT DATABASE CODE


                        //getting uid of user
                        val user=FirebaseAuth.getInstance().currentUser
                        var useremail= user?.email
                        var uid=FirebaseAuth.getInstance().currentUser?.uid.toString()
                        //Toast.makeText(this, uid, Toast.LENGTH_SHORT).show()


                        var database=Firebase.database("https://material-ba9f6-default-rtdb.asia-southeast1.firebasedatabase.app" )
                        val myRef = database.getReference("EMAIL ID").child(uid)
                        var value =HashMap<String,String>()
                        value.put("EMAIL ID",email1)
                        value.put("NAME",name1)
                        value.put("GENDER",gender)
                        value.put("PHONE NUMBER",number1)
                        value.put("PASSWORD",pass1)

                        myRef.setValue(value)

                        var values=Intent(this, Sign::class.java)
                        startActivity(values )
                        Toast.makeText(this, "REGISTERED SUCCESFULLY", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "FIREBASE ERROR", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
        }
    }



