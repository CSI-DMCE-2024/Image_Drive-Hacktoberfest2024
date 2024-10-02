package com.example.material.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat
import com.example.material.R

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        var reg=findViewById<Button>(R.id.button2)
        var sign=findViewById<Button>(R.id.button4)

        reg.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }

        sign.setOnClickListener {
            startActivity(Intent(this, Sign::class.java))
        }
    }

        override fun onStart() {
        super.onStart()
        val sharedPreferences= getSharedPreferences("counter", MODE_PRIVATE)
        val flag=sharedPreferences.getBoolean("flag",false)
        if (flag)
        {
            startActivity(Intent(this, MainActivity3::class.java))
            }
        }
}
